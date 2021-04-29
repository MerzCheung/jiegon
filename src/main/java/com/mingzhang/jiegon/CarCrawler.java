package com.mingzhang.jiegon;

import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.mingzhang.jiegon.dao.CrawlerDao;
import com.mingzhang.jiegon.entity.*;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.seimicrawler.xpath.JXDocument;
import cn.wanghaomiao.seimi.annotation.Crawler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @author merz
 * @Description:
 */
@Slf4j
@Crawler(name = "basic")
public class CarCrawler extends BaseSeimiCrawler {

    @Autowired
    private CrawlerDao crawlerDao;

    public static final String TYPE1_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type1&brand_id=%s";
    public static final String TYPE2_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type2&type1_id=%s";
    public static final String CAR_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type2&type1_id=%s&op_id=%s&year_id=%s";
    public static final String ACCUMULATOR_URL = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&type1_id=%s&op_id=%s&year_id=%s&type2_id=%s";

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
                    crawlerDao.saveCarList(carListEntity);
                    if (!"热门品牌".equals(layerModel)) {
                        push(Request.build(String.format(TYPE1_URL, id), CarCrawler::getType1));
                    }
                }
            }
            System.out.println("start done");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getType1(Response response) {
        String url = response.getRealUrl();
        try {
            String carListId = url.substring(url.lastIndexOf("brand_id=") + 9);
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
                    crawlerDao.saveCarClass(carClassEntity);
                    push(Request.build(String.format(TYPE2_URL, id), CarCrawler::getType2));
                }
            }
            System.out.println("getType1 done");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("url: {}", url);
            throw e;
        }
    }

    public void getType2(Response response) {
        String url = response.getRealUrl();
        try {
            String carClassId = url.substring(url.lastIndexOf("type1_id=") + 9);
            JXDocument doc = response.document();
            List<String> years = new ArrayList<>();
            List<String> ccs = new ArrayList<>();
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
                        if (!carYearEntity.getId().equals(0)) {
                            years.add(carYearEntity.getId().toString());
                        }
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
                        if (!carCcEntity.getId().equals(0)) {
                            ccs.add(carCcEntity.getId().toString());
                        }
                    }
                }
            }
            for (String cc : ccs) {
                for (String year : years) {
                    push(Request.build(String.format(CAR_URL, carClassId, cc, year), CarCrawler::getCarList));
                }
            }
            System.out.println("getType2 done");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("url: {}", url);
            throw e;
        }
    }

    public void getCarList(Response response) {
        String url = response.getRealUrl();
        try {
            String carClassId = url.substring(url.indexOf("type1_id=") + 9, url.indexOf("&op_id="));
            String ccId = url.substring(url.indexOf("op_id=") + 6, url.indexOf("&year_id="));
            String yearId = url.substring(url.lastIndexOf("year_id=") + 8);
            JXDocument doc = response.document();
            Object submenulist = doc.selOne("//ul[@class='submenulist']");
            Element submenulistEl = (Element) submenulist;
            Elements children1 = submenulistEl.children();
            for (Element element : children1) {
                Elements submenulistItem = element.getElementsByClass("submenulist_item");
                String name = submenulistItem.get(0).child(0).text();
                CarDetailsEntity carDetailsEntity = new CarDetailsEntity();
                carDetailsEntity.setCarClassId(Integer.valueOf(carClassId));
                carDetailsEntity.setCarYearId(Integer.valueOf(yearId));
                carDetailsEntity.setCarCcId(Integer.valueOf(ccId));
                carDetailsEntity.setId(Integer.valueOf(submenulistItem.get(0).attr("data-id")));
                carDetailsEntity.setName(name);
                crawlerDao.saveCarDetails(carDetailsEntity);
                push(Request.build(String.format(ACCUMULATOR_URL, carClassId, ccId, yearId, submenulistItem.get(0).attr("data-id")), CarCrawler::getAccumulator));
            }
            System.out.println("getCarList done");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("url: {}", url);
            throw e;
        }
    }

    public void getAccumulator(Response response) {
        String url = response.getRealUrl();
        try {
            String carDetailsId = url.substring(url.lastIndexOf("type2_id=") + 9);
            JXDocument doc = response.document();
            List<Object> sel = doc.sel("//div[@class='result-content']/table[1]/tbody/tr");
            CarAccumulatorConfigEntity carAccumulatorConfigEntity = new CarAccumulatorConfigEntity();
            carAccumulatorConfigEntity.setCarDetailsId(Integer.valueOf(carDetailsId));
            for (int i = 0; i < sel.size(); i++) {
                Element el = (Element) sel.get(i);
                String val = el.child(1).text();
                switch (i) {
                    case 0:
                        carAccumulatorConfigEntity.setType(val);
                        break;
                    case 1:
                        carAccumulatorConfigEntity.setName(val);
                        break;
                    case 2:
                        carAccumulatorConfigEntity.setCapacity(val);
                        break;
                    case 3:
                        carAccumulatorConfigEntity.setSpecification(val);
                        break;
                    case 4:
                        carAccumulatorConfigEntity.setPillarType(val);
                        break;
                    case 5:
                        carAccumulatorConfigEntity.setFixedPolarity(val);
                        break;
                    default:
                }
            }
            crawlerDao.saveAccumulatorConfig(carAccumulatorConfigEntity);

            List<Object> sel2 = doc.sel("//div[@class='result-content']/table[2]/tbody/tr");
            for (Object o : sel2) {
                Element el = (Element) o;
                CarAccumulatorListEntity carAccumulatorListEntity = new CarAccumulatorListEntity();
                carAccumulatorListEntity.setId(Integer.valueOf(el.child(0).attr("data-id")));
                carAccumulatorListEntity.setCarDetailsId(Integer.valueOf(carDetailsId));
                carAccumulatorListEntity.setName(el.child(0).child(0).text().trim());
                crawlerDao.saveAccumulatorList(carAccumulatorListEntity);
            }
            System.out.println("getAccumulator done");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("url: {}", url);
            throw e;
        }
    }
}
