import java.util.*;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.browserup.harreader.model.HarEntry;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;
import com.zebrunner.carina.utils.R;

import browsermob.proxy.utils.ProxyServerUtil;


public class SolvdWebsiteProxyTest implements IAbstractTest {


    @Test()
    @MethodOwner(owner = "ykolchyba")
    public void proxyDynamicModeTest() {
        String url = R.CONFIG.get("url");
        final String trackingRequestUrl = R.CONFIG.get("trackingRequestUrl");

        R.CONFIG.put("browserup_proxy", "true", true);
        R.CONFIG.put("proxy_type", "DYNAMIC", true);
        R.CONFIG.put("proxy_port", "0", true);

        WebDriver driver = getDriver();

        ProxyServerUtil proxyServerUtil = new ProxyServerUtil();
        proxyServerUtil.setFilter(trackingRequestUrl);

        driver.get(url);

        List<HarEntry> harEntries = proxyServerUtil.getEntriesByRqPattern(trackingRequestUrl);
        Assert.assertFalse(harEntries.isEmpty(), "Requests do not contain searched URL part");

        String payload = harEntries.get(0).getRequest().getPostData().getText();
        Assert.assertFalse(getPropertyValueFromJson(payload).isEmpty(), "Payload does not contain a non-empty data property)");
    }

    private String getPropertyValueFromJson(String jsonString) {
        JsonObject jsonObject = new Gson().fromJson(jsonString, JsonObject.class);
        JsonElement element = jsonObject.get("domain");
        if (element != null && !element.isJsonNull()) {
            return element.getAsString();
        }
        return "";
    }
}
