package sun.module.httpclient.post;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by root on 2015/12/28.
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {

        String str = URLDecoder.decode("admanageType=C; bigShowState=; yg_sid=daecceb9-f56c-4e98-89bc-54de538793ac; vizNclk=Fri, 26 Feb 2016 14:20:58 GMT_1; JSESSIONID=763AE7A36AC6130189F546D9A5A46A91; _jzqco=%7C%7C%7C%7C1451312456740%7C1.1571787139.1451312456741.1451312456741.1451312463432.1451312456741.1451312463432.0.0.0.2.2; __v=1.3499194557619288600.1451312464.1451312464.1451312464.1; Hm_lvt_bc66790de6f87c591da5936f04e03efb=1451312463; Hm_lpvt_bc66790de6f87c591da5936f04e03efb=1451312464; __yga=AD_sxhxwXofQ.%28not%2520set%29; __ygrd=1451312682134; __utma=95907011.1558877581.1451312456.1451312456.1451312456.1; __utmb=95907011.11.10.1451312456; __utmc=95907011; __utmz=95907011.1451312456.1.1.utmcsr=AD_sxhxwXofQ|utmccn=(not%20set)|utmcmd=(not%20set)|utmctr=%E4%BC%98%E8%B4%AD%E7%BD%91", "utf-8");
        System.out.println(str);
        System.out.println(URLDecoder.decode(str, "utf-8"));
    }
}
