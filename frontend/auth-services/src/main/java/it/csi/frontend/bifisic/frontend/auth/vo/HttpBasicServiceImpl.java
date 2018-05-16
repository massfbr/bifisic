package it.csi.frontend.bifisic.frontend.auth.vo;

import org.springframework.stereotype.Repository;

@Repository
public class HttpBasicServiceImpl implements HttpBasicService {

	protected HttpBasicRole role = null;
	protected String serverUrl = null;
	
	@Override
	public void initializeFromConfig(String serverUrl) {
		this.serverUrl = serverUrl;

	}

}
