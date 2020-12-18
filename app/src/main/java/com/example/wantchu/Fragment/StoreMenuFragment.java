package com.example.wantchu.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.wantchu.Adapter.MenuListAdapter;
import com.example.wantchu.AdapterHelper.ListMenuHelperClass;
import com.example.wantchu.Database.SessionManager;
import com.example.wantchu.HelperDatabase.StoreCategories;
import com.example.wantchu.HelperDatabase.StoreDetail;
import com.example.wantchu.HelperDatabase.StoreMenus;
import com.example.wantchu.OrderDetails;
import com.example.wantchu.ProgressApplication;
import com.example.wantchu.R;
import com.example.wantchu.Url.UrlMaker;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import maes.tech.intentanim.CustomIntent;

public class StoreMenuFragment extends Fragment implements MenuListAdapter.OnListItemSelectedInterfaceForMenu, MenuListAdapter.OnListItemLongSelectedInterfaceForMenu {
    private final static int SEND_CODE = 1;
    //RecyclerView 설정
    TabLayout mCategoryTabLayout;
    RecyclerView mRecyclerViewMenu;
    Context mContext;
    //JSONparsing
    StoreDetail storeDetail = null;
    StoreCategories storeCategories = null;
    StoreMenus storeMenus = null;

    MenuListAdapter mMenuAdapter;

    //Categories
    ArrayList<StoreCategories> saveCategories = new ArrayList<>();
    //Menus
    ArrayList<StoreMenus> saveMenus = new ArrayList<>();

    String storedIdStr;

    //세션에서 회원 휴대폰값 가져오기
    SessionManager sessionManager;
    String _phone;

    ProgressApplication progressApplication;
    FragmentManager fm;
    BottomMenu bottomMenu;
    View rootView;
    public StoreMenuFragment() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_store_info, container, false);
        mCategoryTabLayout = rootView.findViewById(R.id.category_layout);
        mRecyclerViewMenu = rootView.findViewById(R.id.menu_list);
        drawStoreInfo(Integer.parseInt(storedIdStr));
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("onCreate", true+"");
        sessionManager = new SessionManager(mContext, SessionManager.SESSION_USERSESSION);
        sessionManager.getUsersSession();
        HashMap<String, String> hashMap = sessionManager.getUsersDetailFromSession();
        _phone = hashMap.get(SessionManager.KEY_PHONENUMBER);
        //즐겨찾기 연결
        Intent intent = getActivity().getIntent();
        storedIdStr=intent.getStringExtra("store_id");


    }
    private void setDrawStoreInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(storeDetail != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                drawCategoryInfo(storeDetail.getStore_id());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void setMRecyclerViewMenu(int number) {
        mRecyclerViewMenu.setHasFixedSize(true);
        ArrayList<ListMenuHelperClass> DataList = new ArrayList<>();
        for (int i = 0; i < saveMenus.size(); i++) {

            if(saveMenus.get(i).getCategoryId() == number) {
                StoreMenus storeMenus = saveMenus.get(i);

                mRecyclerViewMenu.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                ListMenuHelperClass listMenuHelperClass = new ListMenuHelperClass(storeMenus.getMenuName(),
                        storeMenus.getMenuDefaultprice(), storeMenus.getMenuId(), storeMenus.getMenuImage(),storeMenus.getMenu_info(),storeMenus.getIs_soldout());
                listMenuHelperClass.storeMenusName.add(storeMenus.getMenuName());
                listMenuHelperClass.storeMenusPrice.add(storeMenus.getMenuDefaultprice());
                listMenuHelperClass.storeMenusId.add(storeMenus.getMenuId());

                DataList.add(listMenuHelperClass);
            }
        }
        mMenuAdapter = new MenuListAdapter(DataList, this, this, getContext(), storedIdStr);
        mRecyclerViewMenu.setAdapter(mMenuAdapter);
    }
    @SuppressLint({"UseCompatLoadingForDrawables", "ResourceAsColor"})
    private void setMRecyclerViewCategory() {
        for(int i = 0; i < saveCategories.size(); i++){
            View tabView = LayoutInflater.from(getActivity()).inflate(R.layout.category_layout, null);
            StoreCategories storeCategories = saveCategories.get(i);

            TextView categoryId= tabView.findViewById(R.id.category_id);
            TextView categoryName = tabView.findViewById(R.id.category_button);

            categoryId.setText(Integer.toString(storeCategories.getCategoryId()));
            categoryName.setText(storeCategories.getCategoryName());
            categoryName.setTextColor(getResources().getColor(R.color.indicator_unselect_text_color));
            categoryName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

            ViewGroup.LayoutParams width = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tabView.setLayoutParams(width);
            TabLayout.Tab myTab = mCategoryTabLayout.newTab().setCustomView(tabView);

            mCategoryTabLayout.addTab(myTab);
            mCategoryTabLayout.bringToFront();
        }


        TextView firstTextView = mCategoryTabLayout.getTabAt(0).view.findViewById(R.id.category_button);
        firstTextView.setTextColor(getResources().getColor(R.color.white));
        mCategoryTabLayout.setSelectedTabIndicator(getResources().getDrawable(R.drawable.layout_radius_3dp_color_main));
        mCategoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                TextView textColor = tab.view.findViewById(R.id.category_button);
                textColor.setTextColor(getResources().getColor(R.color.white));
                int categroyId = Integer.parseInt(((TextView)tab.view.findViewById(R.id.category_id)).getText().toString());
                setMRecyclerViewMenu(categroyId);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                TextView textColor = tab.view.findViewById(R.id.category_button);
                textColor.setTextColor(getResources().getColor(R.color.indicator_unselect_text_color));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                TextView textColor = tab.view.findViewById(R.id.category_button);
                textColor.setTextColor(getResources().getColor(R.color.white));
            }
        });
    }
    private void drawMenusInfo(int number) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        final StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append(getActivity().getString(R.string.menuFindByStoreId));
        stringBuilder.append(number);
        makeRequestGetMenu(stringBuilder.toString());
    }
    //JSON parsing 구간
    private void jsonParsingMenu(String result) {
        int storeId = 0;
        int categoryId = 0;
        String menuInfo = null;
        String name = null;
        int menuDefaultprice = 0;
        int menu_id = 0;
        String menu_image = null;
        String menu_info = null;
        String is_soldout = null;

        try {
            JSONArray jsonArray = new JSONObject(result).getJSONArray("menu");
            for(int i = 0 ; i < jsonArray.length() ; i++) {

                JSONObject jobj = jsonArray.getJSONObject(i);
                storeId = jobj.optInt("store_id");
                categoryId = jobj.optInt("category_id");
                menuInfo = jobj.optString("menu_info");
                name =jobj.optString("menu_name");
                menuDefaultprice = jobj.optInt("menu_defaultprice");
                menu_id = jobj.optInt("menu_id");
                menu_image = jobj.optString("menu_image");
                menu_info = jobj.optString("menu_info");
                is_soldout = jobj.optString("is_soldout");

                storeMenus = new StoreMenus(storeId, categoryId, menuInfo, name, menuDefaultprice, menu_id, menu_image,menu_info,is_soldout);
                saveMenus.add(storeMenus);
                setMRecyclerViewMenu(saveCategories.get(0).getCategoryId());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void drawCategoryInfo(int number) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        final StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append(getActivity().getString(R.string.categoryFindByStoreId));
        stringBuilder.append(number);

        makeRequestGetCategory(stringBuilder.toString());
    }
    //JSON parsing 구간
    private void jsonParsingCategory(String result) {
        boolean parsingResult = false;
        int storeId = 0;
        String name = null;
        int categoryId = 0;
        int firstOpen = 0;
        try {
            JSONObject jsonObject = new JSONObject(result);
            parsingResult=jsonObject.getBoolean("result");
            if(!parsingResult) {
                if(progressApplication != null) {
                    progressApplication.progressOFF();
                }
                Toast.makeText(getActivity(), "로딩에 실패했습니다.", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONArray jsonArray = new JSONObject(result).getJSONArray("category");
            for (int i = 0; i < jsonArray.length() ; i++) {
                storeCategories = new StoreCategories();
                JSONObject jobj = jsonArray.getJSONObject(i);
                storeId = jobj.optInt("store_id");
                name = jobj.optString("category_name");
                categoryId = jobj.optInt("category_id");
                storeCategories.setCategoryId(storeId);
                storeCategories.setCategoryName(name);
                storeCategories.setCategoryId(categoryId);

                saveCategories.add(storeCategories);
                //setDrawCategory(i, jsonArray.length(), name);
            }
            setMRecyclerViewCategory();
            drawMenusInfo(storeId);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //url 설정 및 volley 호출
    private void drawStoreInfo(final int number) {
        UrlMaker urlMaker = new UrlMaker();
        String url = urlMaker.UrlMake("");
        final StringBuilder stringBuilder = new StringBuilder(url);
        stringBuilder.append(getActivity().getString(R.string.storeFindById));
        stringBuilder.append(number);

        makeRequestGetStore(stringBuilder.toString(), number);
    }
    //JSON parsing 구간
    private void jsonParsingStore(String result) {
        Gson gson = new Gson();
        storeDetail = gson.fromJson(result, StoreDetail.class);
        setDrawStoreInfo();
    }
    @Override
    public void onItemSelectedForMenu(View v, int position) {
        MenuListAdapter.MenuViewHolder viewHolder = (MenuListAdapter.MenuViewHolder)mRecyclerViewMenu.findViewHolderForAdapterPosition(position);
        String getMenuName = viewHolder.menuName.getText().toString();

        String getMenuPrice = viewHolder.menuPrice.getText().toString();
        StringTokenizer stringTokenizer = new StringTokenizer(getMenuPrice, " 원");
        String str =stringTokenizer.nextToken();
        Log.i("StringTokenizer", str);

        String getMenuId = viewHolder.menuId.getText().toString();

        Intent intent = new Intent(getActivity(), OrderDetails.class);
        intent.putExtra("menuName", getMenuName);
        intent.putExtra("menuDefaultPrice", Integer.parseInt(str));
        intent.putExtra("menuId", Integer.parseInt(getMenuId));
        intent.putExtra("storeName", storeDetail.getStore_name());
        intent.putExtra("storeId", storeDetail.getStore_id());
        intent.putExtra("storeNumber",storeDetail.getStore_phone());//가게 전화번호

        startActivity(intent);
        CustomIntent.customType(mContext,"left-to-right");
    }

    @Override
    public void onItemLongSelectedForMenu(View v, int adapterPosition) {

    }
    //--------------------------------------------------------------------------------------
    ///Volley 구간 -----------------------------------------------------------------------
    //상점 Volley : StoreFindById.do?store_id=가게id값
    public void makeRequestGetStore(String url, final int number) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsingStore(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }

    //카테고리 Volley : CategoryFindByStoreId.do?store_id=가게id값
    public void makeRequestGetCategory(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        jsonParsingCategory(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }

    //메뉴 Volley : http://15.165.22.64:8080/MenuFindByStoreId.do?store_id=가게id값
    public void makeRequestGetMenu(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //printLog(response);
                        jsonParsingMenu(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("type", "error");
                    }
                });
        //request.setShouldCache(false);
        requestQueue.add(request);
    }
    ///Volley 구간 -----------------------------------------------------------------

    public void printLog(String s) {
        final int MAX_LEN = 2000; // 2000 bytes 마다 끊어서 출력
        int len = s.length();
        if(len > MAX_LEN) {
            int idx = 0, nextIdx = 0;
            while(idx < len) {
                nextIdx += MAX_LEN;
                Log.i("LOG : ", s.substring(idx, nextIdx > len ? len : nextIdx));
                idx = nextIdx;
            }
        }
    }
}
