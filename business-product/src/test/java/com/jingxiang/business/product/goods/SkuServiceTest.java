package com.jingxiang.business.product.goods;

import com.jingxiang.business.id.IdFactory;
import com.jingxiang.business.product.common.consts.ProductConsts;
import com.jingxiang.business.product.common.vo.SkuVo;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * SKU接口单元测试类
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SkuServiceTest {

    @Autowired
    private SkuService skuService;

    @Autowired
    private SkuRepository skuRepository;

    @Test
    public void save() {
        Sku sku = new Sku();
        sku.setId(IdFactory.createProductId(ProductConsts.ID_PREFIX_SKU));
        sku.setShopId("S001");
        sku.setName("iphoneX");
        sku.setDescription("Iphone good good good");
        sku.setSalePrice(new BigDecimal("100.31"));
        sku.setUnit("个");
        sku.setImages("http://1.jpg;http://2.jpg");
        sku.setPic("http://3.jpg");
        sku.setStock(new BigDecimal(100));
        skuService.save(sku);
        Sku dbSku = skuRepository.findOne(sku.getId());
        assertThat(dbSku, equalTo(sku));
        skuRepository.delete(dbSku.getId());
    }

    @Test
    public void updatePublishTime() {
        LocalDateTime publishTime = LocalDateTime.now();
        boolean isSuccess = skuService.updatePublishTime("S001", "K0001", publishTime);
        assertThat(isSuccess, is(true));
        Assert.assertTrue(isSuccess);
        Sku dbSku = skuRepository.findOne("K0001");
        assertThat(dbSku.getPublishTime(), equalTo(publishTime));
    }

    @Test
    public void query() {
        Page<Sku> skuPage = skuService.query("S001", new PageRequest(0, 10));
        assertThat(skuPage.getTotalElements(), is(1L));
        assertThat(skuPage.getContent().get(0).getId(), equalTo("K0001"));
    }

    @Test
    public void queryNull() {
        Page<Sku> skuPage = skuService.query("S002", new PageRequest(0, 10));
        assertThat(skuPage.getTotalElements(), is(0L));
        assertThat(skuPage.getContent(), empty());
    }

    @Test
    public void queryVo() {
        List<SkuVo> skuList = skuService.queryVo("S001", Arrays.asList("K0001", "K0002"));
        assertThat(skuList.size(), is(1));
    }

    @Test
    public void publish() {
        Sku sku = skuService.publish("S001", "K0001");
        assertThat(sku.getId(), equalTo("K0001"));
    }
}