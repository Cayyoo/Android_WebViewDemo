package com.example.webview.four;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webview.R;

/**
 * Cookie相关的Http头
 *
 * 有连个Http头部和Cookie有关：Set-Cookie和Cookie。
 * Set-Cookie由服务器发送，它包含在响应请求的头部中。它用于在客户端创建一个Cookie
 * Cookie头由客户端发送，包含在HTTP请求的头部中。注意，只有cookie的domain和path与请求的URL匹配才会发送这个cookie。
 */

 /**
 * Set-Cookie Header
  *
 * Set-Cookie响应头的格式如下所示:
 * Set-Cookie: =[; =]...
 * [; expires=][; domain=][; path=][; secure][; httponly]
 *
 * expires=: 设置cookie的有效期，如果cookie超过date所表示的日期时，cookie将失效。
 *   如果没有设置这个选项，那么cookie将在浏览器关闭时失效。
 *   注意：date是格林威治时间(GMT)，使用如下格式表示：DAY, DD MMM YYYY HH:MM:SS GMT
 *
 * DAY
 * The day of the week (Sun, Mon, Tue, Wed, Thu, Fri, Sat).
 *
 * DD
 * The day in the month (such as 01 for the first day of the month).
 *
 * MMM
 * The three-letter abbreviation for the month (Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Oct, Nov, Dec).
 *
 * YYYY
 * The year.
 *
 * HH
 * The hour value in military time (22 would be 10:00 P.M., for example).
 *
 * MM
 * The minute value.
 *
 * SS
 * The second value.
 *
 * domain= :
 * path=:
 * 注：临时cookie(没有expires参数的cookie)不能带有domain选项。
 * 当客户端发送一个http请求时，会将有效的cookie一起发送给服务器。
 * 如果一个cookie的domain和path参数和URL匹配，那么这个cookie就是有效的。
 * 一个URL中包含有domain和path，可以参考http://www.w3school.com.cn/html/html_url.asp
 *
 * secure : 表示cookie只能被发送到http服务器。
 * httponly : 表示cookie不能被客户端脚本获取到。
 */

/**
 * 在程序中生成expires:
 * 1、C的方式
 *   time_t curTime = time(NULL);
 *   tm * gmTime = gmtime(&curTime);
 *   char strExperis[50];
 *   strftime(strTimeBuf, 100, " %a, %d %b %Y %X GMT;", gmTime);
 * 2、JavaScript的方式
 *   var d = new Date();
 *   var expires = d.toGMTString();
 */





/**
 * 在进行APP+H5混合开发的时候，一些功能是用native方法实现的,如登陆，一些功能是用H5实现的。
 * 所以往往需要将在native方法登陆的状态同步到H5中避免再次登陆。
 * 这种情况在Android开发中比较常见，因为Android不会自动同步cookie到WebView。
 * 做iOS开发则不用担心这个问题，因为iOS内部已经实现了cookie同步。
 * 本文将会介绍两种cookie同步的方式，并重点分析WebView的cookie机制。
 * 在开始之前先讲一下基于session的登录验证。
 *
 * 基于session的登录验证：
 * 基于session的登录验证，会在程序请求接口的时候判断服务器端是否有当前会话的session，如果没有则被认为没有登录。
 * 客户端没有session这一概念，但有cookie与其对应。每一个session都有一个session id作为唯一标识。
 * 在登录成功后服务器会在请求头中返回cookie，cookie包含着这次登录会话的session id，在接下来的请求中只需要将登陆返回的cookie设置到请求头中便可以通过验证。
 */

/**
 * WebView cookie同步方式一：客户端将cookie传给H5
 *
 * 1、如何做：
 * 客户端：将登陆时从服务器取得的cookie传给html。
 * html：ajax从参数中取出客户端传来的cookie，ajax发请求时将客户端传来cookie设置到请求头中。
 *
 * ajax修改cookie的方式：
 * $.ajax({
 *    headers: {'Cookie' : document.cookie },
 *    url: "sub.domain.com",
 *    success: function(){}
 * })
 *
 * 2、缺点：
 * 兼容性差，多数浏览器为了安全起见，都做了禁止修改请求中的cookie的限制。比如iOS的WebView会拦截ajax修改的cookie。
 * 繁琐，每次请求都需要拼接cookie作为参数，比较繁琐。
 */

/**
 * WebView cookie同步方式二：将cookie同步到WebView(推荐)
 *
 * 1、原理分析：
 * WebView的cookie机制：
 * WebView是基于webkit内核的UI控件，相当于一个浏览器客户端。它会在本地维护每次会话的cookie(保存在data/data/package_name/app_WebView/Cookies.db)。
 * 当WebView加载URL的时候,WebView会从本地读取该URL对应的cookie，并携带该cookie与服务器进行通信。
 * WebView通过android.webkit.CookieManager类来维护cookie。CookieManager是WebView的cookie管理类。
 *
 * 2、如何做：
 * 下面我们就通过CookieManager将cookie同步到WebView中。
 * 之前同步cookie需要用到CookieSyncManager类，现在这个类已经被deprecated。
 * 如今WebView已经可以在需要的时候自动同步cookie了，所以不再需要创建CookieSyncManager类的对象来进行强制性的同步cookie了。
 * 现在只需要获得 CookieManager的对象将cookie设置进去就可以了。
 *
 * （1）：登录时从服务器的返回头中取出cookie
 * 根据Http请求的客户端不同，取cookie的方式也不同，我就不一一罗列了，需要的网友可以自行Google，以HttpURLcollection为例：String cookieStr = conn.getHeaderField("Set-Cookie");
 * （2）：将cookie同步到WebView中
 *  @param url WebView要加载的url
 *  @param cookie 要同步的cookie
 *  @return true 同步cookie成功，false同步cookie失败
 * public static boolean syncCookie(String url,String cookie) {
 *     if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
 *          CookieSyncManager.createInstance(context);
 *     }
 *
 *     CookieManager cookieManager = CookieManager.getInstance();
 *     cookieManager.setCookie(url, cookie);//如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可
 *     String newCookie = cookieManager.getCookie(url);
 *
 *     return TextUtils.isEmpty(newCookie)?false:true;
 * }
 * 如果设置成功，通过cookieManager.getCookie(url)方法就可取得刚才设置的cookie，如果两次设置cookie的url相同，则CookieManager会将上一次设置的cookie覆盖，以达到更新的效果。
 *
 * 3、优点：
 * 方便，只需要在登陆后将cookie同步到WebView即可，省去了每次请求都需要设置一次的繁琐。
 * 兼容性好，因为是系统原生支持的，所以兼容性自然比“客户端将cookie传给H5”要好，不存在cookie被拦截的问题。
 *
 * 4、注意：
 * 同步cookie要在WebView加载url之前，否则WebView无法获得相应的cookie，也就无法通过验证。
 * 每次登录成功后都需要调用"syncCookie"方法将cookie同步到WebView中，同时也达到了更新WebView的cookie。如果登录后没有及时将cookie同步到WebView可能导致WebView拿的是旧的session id和服务器进行通信。
 */
public class FourActivity extends Activity {

    private String strUrl="http://www.baidu.com";

    //华为开发者联盟
    private String urlHome="http://developer.huawei.com/consumer/cn/devunion/ui/server/App_AccessServer.html";
    private String urlLogin="https://hwid1.vmall.com/CAS/portal/loginAuth.html?validated=true&themeName=red&service=https%3A%2F%2Flogin1.vmall.com%2Foauth2%2Flogin%3Fclient_id%3D6099200%26response_type%3Dtoken%26redirect_uri%3Dhttp%253A%252F%252Fdeveloper.huawei.com%252Fdevunion%252FopenPlatform%252Fhtml%252FhandleLogin.html%253Fp%253Dhttp%2525253A%2525252F%2525252Fdeveloper.huawei.com%2525252Fconsumer%2525252Fcn%2525252Fdevunion%2525252Fui%2525252Fserver%2525252FApp_AccessServer.html%26h%3D1482920632.0709%26v%3D093d74f5210ff341ab733e8389899caa6155b8839df2ab9351933e0478dba7bf&loginChannel=89000000&reqClientType=89";

    private TextView txtTitle;
    private ProgressBar loadingProgress;
    private WebView webview;

    private String mytitle;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_four);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        webview = (WebView) findViewById(R.id.wv);
        loadingProgress = (ProgressBar) findViewById(R.id.loadingProgress);// 此控件为一个进度条控件，属于自己添加的控件

        // 设置WebView属性，能够执行Javascript脚本
        webview.getSettings().setJavaScriptEnabled(true);

        loadURL();//加载网页



        // 此处能拦截超链接的url,即拦截href请求的内容.
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                mytitle = title;

                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int progress) {
                txtTitle.setText("");
                loadingProgress.setVisibility(View.VISIBLE);
                loadingProgress.setProgress(progress * 100);

                if (progress == 100) {
                    loadingProgress.setVisibility(View.GONE);
                    txtTitle.setText(mytitle);
                }

                super.onProgressChanged(view, progress);
            }

        });

        /**
         * 如果希望点击链接由自己处理，而不是新开Android的系统browser中响应该链接。
         * 需要给WebView添加一个事件监听对象（WebViewClient)，并重写shouldOverrideUrlLoading方法.
         */
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);

                return true;
            }
        });

    }

    /**
     * 加载网页，保存Cookie
     */
    private void loadURL() {
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String CookieStr = cookieManager.getCookie(urlHome); //获取cookie

        Log.i("Tiger", "onCreate cookie:" + CookieStr);

        if(TextUtils.isEmpty(CookieStr)) {
            webview.loadUrl(urlLogin);

            Toast.makeText(FourActivity.this, "未保存过Cookie", Toast.LENGTH_SHORT).show();
        } else{
            webview.loadUrl(urlHome);

            Toast.makeText(FourActivity.this,"已保存过Cookie",Toast.LENGTH_SHORT).show();
        }

        //登出时清除Cookie
        //cookieManager.removeSessionCookie();// 移除
        //cookieManager.removeAllCookie();
    }

    //在布局中给"关闭"按钮设置的方法
    public void close(View v) {//注意：必须有参
        FourActivity.this.finish();
    }

    //设置回退，覆盖Activity类的onKeyDown(int keyCoder,KeyEvent event)方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webview.canGoBack()) {
            webview.goBack(); // goBack()表示返回WebView的上一页面

            return true;
        }

        Toast.makeText(FourActivity.this, "正在退出", Toast.LENGTH_LONG).show();//最后一步退出时给出提示

        return super.onKeyDown(keyCode, event);
    }





    /**
     * Sync Cookie
     */
    private void syncCookie(Context context, String url){
        //初始化WebView，否则可能无法保存Cookie
        initWebViewSettings();

        try{
            Log.i("Tiger", url);

            CookieSyncManager.createInstance(context);

            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.setAcceptCookie(true);
            cookieManager.removeSessionCookie();// 移除
            cookieManager.removeAllCookie();
            String oldCookie = cookieManager.getCookie(url);
            if(oldCookie != null){
                Log.i("Tiger", oldCookie);
            }


            StringBuilder sbCookie = new StringBuilder();
            sbCookie.append(String.format("JSESSIONID=%s","INPUT YOUR JSESSIONID STRING"));
            sbCookie.append(String.format(";domain=%s", "INPUT YOUR DOMAIN STRING"));
            sbCookie.append(String.format(";path=%s", "INPUT YOUR PATH STRING"));

            String cookieValue = sbCookie.toString();
            cookieManager.setCookie(cookieValue, cookieValue);
            CookieSyncManager.getInstance().sync();



            String newCookie = cookieManager.getCookie(url);
            if(newCookie != null){
                Log.i("Tiger", newCookie);
            }

            syncCookie(this,url);
        }catch(Exception e){
            Log.e("Tiger", e.toString());
        }
    }

    /**
     * init WebView Settings
     * */
    private void initWebViewSettings(){
//        webview.getSettings().setSupportZoom(true);
//        webview.getSettings().setBuiltInZoomControls(true);
//        webview.getSettings().setDefaultFontSize(12);
//        webview.getSettings().setLoadWithOverviewMode(true);

        // 设置可以访问文件
        webview.getSettings().setAllowFileAccess(true);
        //如果访问的页面中有Javascript，则webview必须设置支持Javascript
        webview.getSettings().setJavaScriptEnabled(true);
        //webview.getSettings().setUserAgentString(MyApplication.getUserAgent());
        webview.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAppCacheEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setDatabaseEnabled(true);
    }


}
