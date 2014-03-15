/**
 *    Copyright (C) 2010 - 2014 VREM Software Development <VREMSoftwareDevelopment@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ca.travelagency;

import java.sql.Connection;

import javax.sql.DataSource;

import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.server.ssl.SslSocketConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.webapp.WebAppContext;
import org.joda.time.Duration;

public enum ServerTester {
	INSTANCE;

	public static final int HTTP_PORT = 8080;
	public static final int HTTPS_PORT = 8443;
	public static final int TIMEOUT = (int) Duration.standardHours(1).getMillis();
	public static final String KEYSTORE_FILENAME = "/keystore.jks";
	public static final String KEYSTORE_PASSWORD = "wicket";

	private Server server;

	private ServerTester()  {
		server = new Server();
		server.setConnectors(createConnectors());
		server.setHandler(createWebAppContext(server));


		// START JMX SERVER
		// MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
		// MBeanContainer mBeanContainer = new MBeanContainer(mBeanServer);
		// server.getContainer().addEventListener(mBeanContainer);
		// mBeanContainer.start();
	}

	public void start() throws Exception {
		if (server.isStopped()) {
			System.out.println(">>> STARTING EMBEDDED JETTY SERVER, PRESS <ENTER> KEY TO STOP");
			setupTestDatabase();
			server.start();
			System.out.println(">>> Application can be accessed using: 'http://localhost:"+HTTP_PORT+"' or SSL 'https://localhost:"+HTTPS_PORT+"'");
			System.out.println(">>> STARTED");
		}
	}

	private void setupTestDatabase() throws Exception {
		DataSource dataSource = SpringLocator.INSTANCE.getDataSource();
        Connection connection = dataSource.getConnection();
		JdbcConnection jdbcConnection = new JdbcConnection(connection);
        FileSystemResourceAccessor resourceAccessor = new FileSystemResourceAccessor();
		Liquibase liquibase = new Liquibase("../db/test/db-changelog.xml", resourceAccessor, jdbcConnection);
		liquibase.update((String) null);
	}

	public void stop() throws Exception {
		if (server.isRunning()) {
			System.out.println(">>> STOPPING EMBEDDED JETTY SERVER");
			server.stop();
			server.join();
			System.out.println(">>> STOPPED");
		}
	}

	private Connector[] createConnectors() {
		// Apache Wicket Quickstart Certificate expires about half way september 2021.
		// Do not use this certificate anywhere important as the passwords are available in the source.
		Resource keystore = Resource.newClassPathResource(KEYSTORE_FILENAME);
		if (keystore == null || !keystore.exists()) {
			System.err.println("Error! Keystore file:'"+KEYSTORE_FILENAME+"' is not available!");
			System.exit(2);
		}

		SocketConnector connector = new SocketConnector();
		connector.setPort(HTTP_PORT);
		connector.setConfidentialPort(HTTPS_PORT);
		connector.setMaxIdleTime(TIMEOUT);
		connector.setSoLingerTime(-1);

        SslContextFactory factory = new SslContextFactory();
        factory.setKeyStoreResource(keystore);
        factory.setKeyStorePassword(KEYSTORE_PASSWORD);
        factory.setTrustStoreResource(keystore);
        factory.setKeyManagerPassword(KEYSTORE_PASSWORD);

        SslSocketConnector sslConnector = new SslSocketConnector(factory);
        sslConnector.setMaxIdleTime(TIMEOUT);
        sslConnector.setPort(HTTPS_PORT);
        sslConnector.setAcceptors(4);

		return new Connector[] { connector, sslConnector };
	}

	private WebAppContext createWebAppContext(Server server) {
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setServer(server);
		webAppContext.setContextPath("/");
		webAppContext.setWar("src/main/webapp");
		return webAppContext;
	}
}