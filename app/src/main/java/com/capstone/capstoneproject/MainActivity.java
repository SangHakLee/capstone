package com.capstone.capstoneproject;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.skp.Tmap.TMapMarkerItem;
import com.skp.Tmap.TMapPoint;
import com.skp.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // 반월터널 37.333534, 126.880503

    String aa = "bbb";

    private static final String TAG = "Main";

//    private static final double LAT = 37.333534; // 반월터널 정보
    private static final double LAT = 37.476727; // 반월터널 정보
//    private static final double LON = 126.880503; // 반월터널 정보
    private static final double LON = 126.963987; // 반월터널 정보
    private static final double MEASURE_DIST = 200; // 차와 터널 사이에 측정 기준 거리

    private double NOW_LAT;
    private double NOW_LON;

    public static String mApiKey = "d8a122f8-d03f-37ca-968a-aba76934c836";


    /* 차량 연결시 메세지 */ private static final String connectCar= "연결";
    /* 차량 해제시 메세지 */ private static final String disconnectCar= "해제";


    ///////////////////////////////////////////////////////////////////////////////////////////////

    LocationManager manager;

    TMapView tMapView; // T맵

    EditText leftDistEt;

    boolean firstLocation = true;

    View.OnClickListener handler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.onBtn:
                    clickConnectBtn();
                    break;
                case R.id.offBtn:
                    Toast.makeText(MainActivity.this, disconnectCar, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void clickConnectBtn(){
        // 37.330446, 126.884150 현재 위치
//        double returnValue =  calDistance(37.330446, 126.884150, LAT, LON);
//        Toast.makeText(this, returnValue+"", Toast.LENGTH_SHORT).show();

//        if(checkIsEnter(returnValue)){
//            Toast.makeText(this, "터널진입 ~~ 하세요", Toast.LENGTH_SHORT).show();
//
//            // 아두이노 통신.
//        }else{
//            Toast.makeText(this, "아직 터널 아님 더 가세요", Toast.LENGTH_SHORT).show();
//        }
//        Toast.makeText(MainActivity.this, connectCar, Toast.LENGTH_SHORT).show();
    };

    public int calDistance(double lat1, double lon1, double lat2, double lon2){

        double theta, dist;
        theta = lon1 - lon2;
        dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;    // 단위 mile 에서 km 변환.
        dist = dist * 1000.0;      // 단위  km 에서 m 로 변환

//        return (int)dist;
        return (int)Math.round(dist);
    }
    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
    }

    public void doButtonClick() {
//        Toast.makeText(this, "aaa", Toast.LENGTH_SHORT).show();
//        Log.v(TAG, "aaaaa");

//        // 37.330446, 126.884150 현재 위치
//        double returnValue =  calDistance(37.330446, 126.884150, LAT, LON);
//        Toast.makeText(this, returnValue + "", Toast.LENGTH_SHORT).show();
//
//        if(checkIsEnter(returnValue)){
//            Toast.makeText(this, "터널진입 ~~ 하세요", Toast.LENGTH_SHORT).show();
//
//            // 아두이노 통신.
//        }else{
////            Toast.makeText(this, "아직 터널 아님 더 가세요", Toast.LENGTH_SHORT).show();
//        }

//        Toast.makeText(this, "차량이 연결되었습니다.", Toast.LENGTH_LONG).show();

    };

    public String calcDist(double lat, double lon){
        double returnValue =  calDistance(lat, lon, LAT, LON);
        String str = "현재 거리 : "+ returnValue;
        if(checkIsEnter(returnValue)){
            str = str + " 터널입니다. 창문을 닫아주세요.";
            enterTunnel();
        }else{
            str = str + " 아직 터널 아님";
        }
        return str;
    };

    public void enterTunnel(){
        // 아두이노랑 연결
    };

    public boolean checkIsEnter(double dist){
        if(dist < MEASURE_DIST){
            return true;
        }else{
            return false;
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(TAG, requestCode + "    requestCode");
        Log.v(TAG, resultCode+"    resultCode");
        switch (requestCode){
            case 999:
                switch (resultCode){
                    case RESULT_OK:
//                        Toast.makeText(this, "설정완료", Toast.LENGTH_SHORT).show();
                        break;
                    default:
//                        Toast.makeText(this, "GPS 사용불가", Toast.LENGTH_SHORT).show();
                        break;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double nowLat = location.getLatitude();
            double nowLon = location.getLongitude();
//            String  str = String.format("위도 %f, 경도 %f", location.getLatitude(), location.getLongitude());
//            Log.v(TAG, str);

            int returnValue =  calDistance(LAT, LON, nowLat, nowLon);
            Log.v(TAG, "onLocationChanged !!");
            Log.v(TAG, "==??         : " + returnValue);

            leftDistEt.setText("" + returnValue);

//            calcDist(location.getLatitude(), location.getLongitude());
//            et2.setText(calcDist(location.getLatitude(), location.getLongitude()));

            if(firstLocation){
                Log.v(TAG, "firstLocation");
//                initView(location.getLongitude(), location.getLatitude());
                firstLocation = false;
            }
            initView(nowLat, nowLon);

            calcDist(nowLat, nowLon);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 1, locationListener); // 마시멜로 버전은 무조건 실행되지 않고 버전 체크 해야한다.
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        manager.removeUpdates(locationListener);
    }


    private void configureMapView() {
        tMapView.setSKPMapApiKey(mApiKey);
    }

    // 티맵 init
    public void initView(double lat, double lon){
        configureMapView();
        tMapView.setLocationPoint(lat, lon); // 현재위치
        tMapView.setCenterPoint(lat, lon);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car);
        tMapView.setIcon(bitmap);
        tMapView.setIconVisibility(true);
        tMapView.setSightVisible(true);

        tMapView.setCompassMode(true); // 나침반 모드
        tMapView.setTrackingMode(true); // 트래킹모드


        TMapMarkerItem markeritem = new TMapMarkerItem();
        tMapView.addMarkerItem("TestID", markeritem);
//        tMapView.setCompassMode(false);
//        tMapView.setTrafficInfo(true);

        showMarkerPoint();
    }

    public void showMarkerPoint() {

        Bitmap bitmap_i = BitmapFactory.decodeResource(getResources(), R.mipmap.tunnel); // > 아이콘



        TMapPoint point = new TMapPoint(37.316953, 126.830440);
        TMapMarkerItem item1 = new TMapMarkerItem();

        item1.setTMapPoint(point);
        item1.setName("안산터널");
        item1.setVisible(item1.VISIBLE);
        item1.setIcon(bitmap_i);
        item1.setCalloutTitle("안산터널");
        item1.setCalloutSubTitle("안산터널");
        item1.setCanShowCallout(true);
        item1.setAutoCalloutVisible(true);
        item1.setCalloutRightButtonImage(bitmap_i);

        String strID = String.format("pmarker%d", 1);
        tMapView.addMarkerItem(strID, item1);  // 마커에 아이디를 다르게 해줘야한다. 문자열로

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tMapView = (TMapView)findViewById(R.id.view);



        ////////////////////////////


        // (Button)findViewById(R.id.button); 이 문장이 화면에 있는 거 찾아오기
        findViewById(R.id.onBtn).setOnClickListener(handler);
        findViewById(R.id.offBtn).setOnClickListener(handler);

        leftDistEt = (EditText)findViewById(R.id.leftDistEt);
//        et2 = (EditText)findViewById(R.id.editText2);


        ////////////////////////////

        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
//            Toast.makeText(this, "GPS 사용가능", Toast.LENGTH_SHORT).show();
        }else{
            startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 999);
//            Toast.makeText(this, "GPS 사용불가", Toast.LENGTH_SHORT).show();

        }

        // http://hyoin1223.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%EB%B8%94%EB%A3%A8%ED%88%AC%EC%8A%A4-%ED%94%84%EB%A1%9C%EA%B7%B8%EB%9E%98%EB%B0%8D
        // 블루투스 연결하기
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter == null) {
            //장치가 블루투스를 지원하지 않는 경우.
        }

        else {
            // 장치가 블루투스를 지원하는 경우.
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
