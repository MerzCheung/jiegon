package com.mingzhang.jiegon;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.mingzhang.jiegon.dao.CrawlerDao;
import com.mingzhang.jiegon.entity.CarCcEntity;
import com.mingzhang.jiegon.entity.CarClassEntity;
import com.mingzhang.jiegon.entity.CarListEntity;
import com.mingzhang.jiegon.entity.CarYearEntity;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import cn.wanghaomiao.seimi.annotation.Crawler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author merz
 * @Description:
 */
@Crawler(name = "basic")
public class CarCrawler extends BaseSeimiCrawler {

    @Autowired
    private CrawlerDao crawlerDao;

    public static final String TYPE1_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type1&brand_id=%s";
    public static final String TYPE2_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type2&type1_id=%s";

    @Override
    public String[] startUrls() {
        return new String[]{"http://www.jiegon.com/mobile/adapter.php?adapter_id=13&cat_id=2&attr_type_id=1"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        try {
            List<Object> eles = doc.sel("//div[@class='model_layer_list']/div[@class='models_cont_item']");
            for (Object ele : eles) {
                Element element = (Element) ele;
                Elements elements = new Elements(element);
                JXDocument jxDocument = new JXDocument(elements);
                String layerModel = jxDocument.selOne("//h5/text()").toString();
                List<Object> models = jxDocument.sel("//ul[@class='models_cont_lists']/li");
                for (Object model : models) {
                    Element element2 = (Element) model;
                    Elements elements2 = new Elements(element2);
                    JXDocument jxDocument2 = new JXDocument(elements2);
                    String img = jxDocument2.selOne("//div[@class='models_pic']/img/@src").toString().replace("./../data/brandlogo/","");
                    String name = jxDocument2.selOne("//p/text()").toString();
                    String id = jxDocument2.selOne("//@data-id").toString();

                    CarListEntity carListEntity = new CarListEntity();
                    carListEntity.setId(Integer.valueOf(id));
                    carListEntity.setLayerModel(layerModel);
                    carListEntity.setName(name);
                    carListEntity.setImg(img);
//                    crawlerDao.saveCarList(carListEntity);
                    if (id.equals("1")) {
                        push(Request.build(String.format(TYPE1_URL, id), CarCrawler::getType1));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getType1(Response response) {
        String carListId = response.getRealUrl().substring(response.getRealUrl().lastIndexOf("=") + 1);
        JXDocument doc = response.document();
        Object ele = doc.selOne("//div[@class='asidesubmenu_lists']");
        Element element = (Element) ele;
        Elements children = element.children();
        String carType = null;
        for (Element el : children) {
            if ("div".equals(el.tagName())) {
                carType = el.child(0).text();
            } else if ("a".equals(el.tagName())) {
                CarClassEntity carClassEntity = new CarClassEntity();
                carClassEntity.setCarListId(Integer.valueOf(carListId));
                carClassEntity.setCarType(carType);
                String href = el.attr("href");
                String id = href.substring(href.lastIndexOf("=") + 1);
                carClassEntity.setId(Integer.valueOf(id));
                carClassEntity.setCarStyle(el.text());
//                crawlerDao.saveCarClass(carClassEntity);
                if (id.equals("1")) {
                    push(Request.build(String.format(TYPE2_URL, id), CarCrawler::getType2));
                }
            }
        }
    }

    public void getType2(Response response) {
        String carClassId = response.getRealUrl().substring(response.getRealUrl().lastIndexOf("=") + 1);
        JXDocument doc = response.document();
        List<Object> sels = doc.sel("//div[@class='asidesubmenu_top_cont']");
        for (int i = 0; i < sels.size(); i++) {
            Element element = (Element) sels.get(i);
            if (i == 0) {
                // 年限
                Elements children = element.children();
                for (Element element2 : children) {
                    CarYearEntity carYearEntity = new CarYearEntity();
                    carYearEntity.setCarClassId(Integer.valueOf(carClassId));
                    carYearEntity.setId(Integer.valueOf(element2.attr("data-id")));
                    carYearEntity.setName(element2.text());
                    crawlerDao.saveCarYear(carYearEntity);
                }
            } else if (i == 1) {
                // 排量
                Elements children = element.children();
                for (Element element2 : children) {
                    CarCcEntity carCcEntity = new CarCcEntity();
                    carCcEntity.setCarClassId(Integer.valueOf(carClassId));
                    String[] onclicks = element2.attr("onclick").split(",");
                    carCcEntity.setId(Integer.valueOf(onclicks[3]));
                    carCcEntity.setName(element2.text());
                    crawlerDao.saveCarCc(carCcEntity);
                }
            }
        }
    }
}
