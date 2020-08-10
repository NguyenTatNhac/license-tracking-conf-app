package com.ntnguyen.app.confluence.licensetracking.macro;

import static com.atlassian.confluence.util.velocity.VelocityUtils.getRenderedTemplate;
import static com.ntnguyen.app.confluence.licensetracking.util.Constants.APP_KEY;

import com.atlassian.confluence.content.render.xhtml.ConversionContext;
import com.atlassian.confluence.macro.Macro;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.webresource.api.assembler.PageBuilderService;
import com.ntnguyen.app.confluence.licensetracking.persistent.entity.LicenseEntity;
import com.ntnguyen.app.confluence.licensetracking.service.LicenseService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LicenseTrackingMacro implements Macro {

  private final PageBuilderService pageBuilderService;
  private final LicenseService licenseService;

  @Autowired
  public LicenseTrackingMacro(@ComponentImport PageBuilderService pageBuilderService,
      LicenseService licenseService) {
    this.pageBuilderService = pageBuilderService;
    this.licenseService = licenseService;
  }

  @Override
  public String execute(Map<String, String> map, String s, ConversionContext conversionContext) {
    Map<String, Object> ctx = new HashMap<>(map);
    List<LicenseEntity> licenses = licenseService.getAll();
    ctx.put("licenses", licenses);
    pageBuilderService.assembler().resources().requireWebResource(APP_KEY + ":common-resources");
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
