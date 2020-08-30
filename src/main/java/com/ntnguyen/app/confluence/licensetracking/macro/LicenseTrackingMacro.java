package com.ntnguyen.app.confluence.licensetracking.macro;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.ntnguyen.app.confluence.licensetracking.util.Constants.APP_KEY;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseTrackingMacro implements Macro {

  private final PageBuilderService pageBuilderService;

  @Autowired
  public LicenseTrackingMacro(@ComponentImport PageBuilderService pageBuilderService) {
    this.pageBuilderService = pageBuilderService;
  }

  @Override
  public String execute(Map<String, String> map, String s, ConversionContext conversionContext) {
    pageBuilderService.assembler().resources().requireWebResource(APP_KEY + ":mlt-macro-resource");
    return getRenderedTemplate("templates/license-macro-view.vm", map);
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
