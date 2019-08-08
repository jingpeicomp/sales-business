package com.jingxiang.business.acct.adapter.wechat;

import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.MessageDigest;
import java.util.*;

import static com.jingxiang.business.acct.adapter.wechat.WxPayConsts.SignType;


@Slf4j
public class WxPayUtil {
    /**
     * XML格式字符串转换为Map
     *
     * @param strXML XML字符串
     * @return XML数据转换后的Map
     */
    public static Map<String, String> xmlToMap(String strXML) {
        Map<String, String> data = new HashMap<>();
        try (InputStream stream = new ByteArrayInputStream(strXML.getBytes("UTF-8"))) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = documentBuilder.parse(stream);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getDocumentElement().getChildNodes();
            for (int idx = 0, length = nodeList.getLength(); idx < length; idx++) {
                Node node = nodeList.item(idx);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    data.put(element.getNodeName(), element.getTextContent());
                }
            }
        } catch (Exception ex) {
            log.error("Fail to convert xml to map", ex);
        }
        return data;
    }

    /**
     * 将Map转换为XML格式的字符串
     *
     * @param data Map类型数据
     * @return XML格式的字符串
     */
    public static String mapToXml(Map<String, String> data) {
        try (StringWriter writer = new StringWriter()) {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.newDocument();
            Element root = document.createElement("xml");
            document.appendChild(root);
            data.forEach((key, value) -> {
                String formattedValue = Optional.ofNullable(value).map(String::trim).orElse("");
                Element filed = document.createElement(key);
                filed.appendChild(document.createTextNode(formattedValue));
                root.appendChild(filed);
            });

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            DOMSource source = new DOMSource(document);
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            StreamResult result = new StreamResult(writer);
            transformer.transform(source, result);
            return writer.getBuffer().toString();
        } catch (Exception e) {
            log.error("Fail to convert map to xml", e);
        }

        return "";
    }


    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key) {
        return generateSignedXml(data, key, SignType.MD5);
    }

    /**
     * 生成带有 sign 的 XML 格式字符串
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名类型
     * @return 含有sign字段的XML
     */
    public static String generateSignedXml(final Map<String, String> data, String key, SignType signType) {
        String sign = generateSignature(data, key, signType);
        data.put(WxPayConsts.FIELD_SIGN, sign);
        return mapToXml(data);
    }


    /**
     * 判断签名是否正确
     *
     * @param xmlStr XML格式数据
     * @param key    API密钥
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(String xmlStr, String key) {
        Map<String, String> data = xmlToMap(xmlStr);
        if (!data.containsKey(WxPayConsts.FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(WxPayConsts.FIELD_SIGN);
        return generateSignature(data, key).equals(sign);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。使用MD5签名。
     *
     * @param data Map类型数据
     * @param key  API密钥
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(Map<String, String> data, String key) {
        return isSignatureValid(data, key, SignType.MD5);
    }

    /**
     * 判断签名是否正确，必须包含sign字段，否则返回false。
     *
     * @param data     Map类型数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名是否正确
     */
    public static boolean isSignatureValid(Map<String, String> data, String key, SignType signType) {
        if (!data.containsKey(WxPayConsts.FIELD_SIGN)) {
            return false;
        }
        String sign = data.get(WxPayConsts.FIELD_SIGN);
        return generateSignature(data, key, signType).equals(sign);
    }

    /**
     * 生成签名
     *
     * @param data 待签名数据
     * @param key  API密钥
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key) {
        return generateSignature(data, key, SignType.MD5);
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     *
     * @param data     待签名数据
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(final Map<String, String> data, String key, SignType signType) {
        Set<String> keySet = data.keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(WxPayConsts.FIELD_SIGN)) {
                continue;
            }
            if (data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
                sb.append(k).append("=").append(data.get(k).trim()).append("&");
        }
        sb.append("key=").append(key);
        if (SignType.MD5.equals(signType)) {
            return toMd5(sb.toString()).toUpperCase();
        } else if (SignType.HMACSHA256.equals(signType)) {
            return toHmacsha256(sb.toString(), key);
        } else {
            throw new IllegalArgumentException(String.format("Invalid sign_type: %s", signType));
        }
    }


    /**
     * 获取随机字符串 Nonce Str
     *
     * @return String 随机字符串
     */
    public static String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }


    /**
     * 生成 toMd5
     *
     * @param data 待处理数据
     * @return MD5结果
     */
    public static String toMd5(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("toMd5");
            byte[] array = md.digest(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            log.error("Convert to md5 error", e);
        }

        return "";
    }

    /**
     * 生成 toHmacsha256
     *
     * @param data 待处理数据
     * @param key  密钥
     * @return 加密结果
     */
    public static String toHmacsha256(String data, String key) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            byte[] array = sha256_HMAC.doFinal(data.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        } catch (Exception e) {
            log.error("Convert to Hmacsha256 error", e);
        }

        return "";
    }
}
