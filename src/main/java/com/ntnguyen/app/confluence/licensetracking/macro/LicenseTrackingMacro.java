package com.ntnguyen.app.confluence.licensetracking.macro;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class LicenseTrackingMacro implements Macro {

  @Override
  public String execute(Map<String, String> map, String s, ConversionContext conversionContext) {
    Map<String, Object> ctx = new HashMap<>(map);
    return getRenderedTemplate("templates/license-macro-view.vm", ctx);
  }

  @Override
  public BodyType getBodyType() {
    return BodyType.NONE;
  }

  @Override
  public OutputType getOutputType() {
    return OutputType.BLOCK;
  }
}
