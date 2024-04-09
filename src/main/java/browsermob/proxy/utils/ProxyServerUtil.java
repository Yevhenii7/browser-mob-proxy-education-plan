package browsermob.proxy.utils;

import java.nio.charset.*;
import java.util.*;
import java.util.stream.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.browserup.bup.BrowserUpProxy;
import com.browserup.bup.proxy.CaptureType;
import com.browserup.harreader.model.HarEntry;
import com.browserup.harreader.model.HttpStatus;
import com.zebrunner.carina.proxy.ProxyPool;
import com.zebrunner.carina.proxy.browserup.CarinaBrowserUpProxy;


public class ProxyServerUtil {

    private static final Logger LOGGER = LogManager.getLogger(ProxyServerUtil.class);
    private List<String> caughtContent = new ArrayList<>();
    private static final String HAR_NAME = "harFile";
    private final BrowserUpProxy proxy;

    public ProxyServerUtil() {
        proxy = ProxyPool.getOriginal(CarinaBrowserUpProxy.class)
                .orElseThrow().getProxy();
    }

    public void setFilter(String key) {
        proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.RESPONSE_CONTENT);
        proxy.newHar();
//        proxy.addRequestFilter((request, contents, messageInfo) -> {
//            if (messageInfo.getOriginalUrl().contains(key)) {
//                String decodedString = java.net.URLDecoder.decode(contents.getTextContents(), StandardCharsets.UTF_8);
//                caughtContent.add(decodedString);
//                LOGGER.info("Caught content: " + decodedString);
//            }
//            return null;
//        });
    }

    public List<HarEntry> getEntriesByRqPattern(final String requestUrlPattern) {
        return proxy.getHar().getLog().getEntries().stream().filter(
                        entry -> entry.getRequest().getUrl().contains(requestUrlPattern))
                .collect(Collectors.toList());
    }
}
