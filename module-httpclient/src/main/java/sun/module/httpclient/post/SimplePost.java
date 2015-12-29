package sun.module.httpclient.post;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2015/12/28.
 */
public class SimplePost {

    private void send() throws IOException {
        String url = "http://www.yougou.com/my/getMobileCode.jhtml";
//        String phone = "15136180579";
        String phone = "13248216196";

        CloseableHttpClient httpClient = HttpClients.createDefault();

        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("Accept", "*/*");
        httpPost.addHeader("Origin", "http://www.sh9199.com");
        httpPost.addHeader("X-Requested-With", "XMLHttpRequest");
        httpPost.addHeader("Connection", "keep-alive");
        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("DNT", "1");
        httpPost.addHeader("HOST","www.yougou.com");
        httpPost.addHeader("Origin","http://www.yougou.com");

        httpPost.addHeader("Referer", "http://www.yougou.com/register.jhtml?redirectURL=http://www.yougou.com/?utm_source=AD_sxhxwXofQ&fc=u800700.k1004100.a0.pb");
        httpPost.addHeader("Accept-Encoding", "gzip, deflate");
        httpPost.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
        httpPost.addHeader("Cookie", "admanageType=C; bigShowState=; yg_sid=daecceb9-f56c-4e98-89bc-54de538793ac; vizNclk=Fri, 26 Feb 2016 14:20:58 GMT_1; _jzqco=%7C%7C%7C%7C1451312456740%7C1.1571787139.1451312456741.1451312456741.1451312463432.1451312456741.1451312463432.0.0.0.2.2; __v=1.3499194557619288600.1451312464.1451312464.1451312464.1; Hm_lvt_bc66790de6f87c591da5936f04e03efb=1451312463; Hm_lpvt_bc66790de6f87c591da5936f04e03efb=1451312464; __yga=AD_sxhxwXofQ.%28not%2520set%29; __ygrd=1451314415203; __utma=95907011.1558877581.1451312456.1451312456.1451312456.1; __utmb=95907011.13.10.1451312456; __utmc=95907011; __utmz=95907011.1451312456.1.1.utmcsr=AD_sxhxwXofQ|utmccn=(not%20set)|utmcmd=(not%20set)|utmctr=%E4%BC%98%E8%B4%AD%E7%BD%91; JSESSIONID=F222103DE23137925F47E5682E53444B");

        List<NameValuePair> formParams = new ArrayList<NameValuePair>();
        formParams.add(new BasicNameValuePair("phone", phone));
        formParams.add(new BasicNameValuePair("codes", "checkCode"));
        formParams.add(new BasicNameValuePair("validCode", "tvaz"));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);
        httpPost.setEntity(formEntity);

        CloseableHttpResponse response = httpClient.execute(httpPost);
        try{
            System.out.println(response.getStatusLine());
            HttpEntity httpEntity = response.getEntity();
            InputStream inputStream = httpEntity.getContent();
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            System.out.println(sb.toString());
            System.out.println("==========");
//            EntityUtils.consume(httpEntity);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            response.close();
        }

        httpClient.close();
    }


    public static void main(String[] args) throws IOException {
        SimplePost simplePost = new SimplePost();
        for(int i = 0; i< 1 ; i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            simplePost.send();
        }

    }
}
