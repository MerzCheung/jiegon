package com.mingzhang.jiegon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class JiegonApplicationTests {

    @Test
    void contextLoads() {
        String s = "http://www.jiegon.com/mobile/adapter.php?act=&adapter_id=13&cat_id=2&attr_type_id=1&search_type=type2&type1_id=1&op_id=2&year_id=5";
        System.out.println(s.substring(s.indexOf("type1_id=") + 9, s.indexOf("&op_id=")));
        System.out.println(s.substring(s.indexOf("op_id=") + 6, s.indexOf("&year_id=")));
        System.out.println(s.substring(s.lastIndexOf("year_id=") + 8));
    }

}
