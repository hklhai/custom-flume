package com.hxqh.custom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.apache.flume.source.http.HTTPSourceHandler;
import org.apache.flume.source.http.JSONHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ocean lin on 2019/4/18.
 *
 * @author Ocean lin
 */
public class HttpSourceJsonHandler implements HTTPSourceHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JSONHandler.class);
//    private final Type listType = new TypeToken<List<JSONEvent>>() {
//    }.getType();

    private final Type type = new TypeToken<Object>() {
    }.getType();

    private final Gson gson;

    public HttpSourceJsonHandler() {
        gson = new GsonBuilder().disableHtmlEscaping().create();
    }

    /**
     * {@inheritDoc}
     */
    public List<Event> getEvents(HttpServletRequest request) throws Exception {
        String charset = request.getCharacterEncoding();

        //UTF-8 is default for JSON. If no charset is specified, UTF-8 is to
        //be assumed.
        if (charset == null) {
            LOG.debug("Charset is null, default charset of UTF-8 will be used.");
            charset = "UTF-8";
        } else if (!(charset.equalsIgnoreCase("utf-8")
                || charset.equalsIgnoreCase("utf-16")
                || charset.equalsIgnoreCase("utf-32"))) {
            LOG.debug("charset:" + charset);

            LOG.error("Unsupported character set in request {}. "
                    + "JSON handler supports UTF-8, "
                    + "UTF-16 and UTF-32 only.", charset);
            throw new UnsupportedCharsetException("JSON handler supports UTF-8, "
                    + "UTF-16 and UTF-32 only.");
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF8"));
        StringBuffer buff = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            buff.append(line);
        }
        String replaceAll = buff.toString().replaceAll("\t", "").replaceAll("    ", "");

        List<Event> newEvents = new ArrayList<Event>(10);
        newEvents.add(EventBuilder.withBody(replaceAll.getBytes()));
        return newEvents;
    }

    public void configure(Context context) {
    }

}
