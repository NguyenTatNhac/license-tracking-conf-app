package com.ntnguyen.app.confluence.licensetracking.model;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@XmlRootElement
public class MarketplaceLicenseResult {

  private List<MarketplaceLicense> licenses;
}
