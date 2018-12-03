package dataconservationnitrr.swcmonitoring;




/**
 * Created by Ketan-PC on 11/1/2017.
 */

public class All_urls {
    public static abstract class values{

 public static final String dataUpload(Double lat,Double lang,String url,String desc,String locality) {
 return "http://eclectika.org/projectData/userdata.php?lat="+lat+"&lon="+lang+"&imgUrl="+url+"&description="+desc+"&checkData=0&locality="+locality;
 }

  }




}
