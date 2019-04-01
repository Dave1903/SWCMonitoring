package dataconservationnitrr.swcmonitoring;




/**
 * Created by Ketan-PC on 11/1/2017.
 */

public class All_urls {
    public static abstract class values{

 public static final String dataUpload(Double lat,Double lang,String url,String desc,String locality) {
 return "http://eclectika.org/projectData/userdata.php?lat="+lat+"&lon="+lang+"&imgUrl="+url+"&description="+desc+"&checkData=0&locality="+locality;
 }
 public static final String mapData="http://eclectika.org/projectData/mapData.php";
     public static final String pendingData="http://eclectika.org/projectData/checkApprove.php?checkData=0";
        public static final String approvedData="http://eclectika.org/projectData/checkApprove.php?checkData=1";

     public static final String approveData(String id) {
      return "http://eclectika.org/projectData/updateCheckData.php?id="+id;
     }

        public static final String detecttags="http://eclectika.org/projectData/imagga.php";

  }




}
