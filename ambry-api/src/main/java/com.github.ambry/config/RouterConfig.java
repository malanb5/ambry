package com.github.ambry.config;

/**
 * Configuration parameters required by a Router.
 * <p/>
 * Receives the in-memory representation of a properties file and extracts parameters that are specifically
 * required for a Router and presents them for retrieval through defined APIs.
 */
public class RouterConfig {

  /**
   * Number of background threads to perform coordinator operations in CoordinatorBackedRouter.
   */
  @Config("router.scaling.unit.count")
  @Default("1")
  public final int routerScalingUnitCount;

  /**
   * The hostname of the node upon which the router runs.
   */
  @Config("router.hostname")
  public final String routerHostname;

  /**
   * The name of the datacenter in which the router is located.
   */
  @Config("router.datacenter.name")
  public final String routerDatacenterName;

  /**
   * The max connections allowed per (datanode, port) for plain text
   */
  @Config("router.max.connections.per.port.plain.text")
  @Default("5")
  public final int routerMaxConnectionsPerPortPlainText;

  /**
   * The max connections allowed per (datanode, port) for ssl
   */
  @Config("router.max.connections.per.port.ssl")
  @Default("2")
  public final int routerMaxConnectionsPerPortSsl;

  /**
   * The max chunk size to be used for put operations.
   */
  @Config("router.max.put.chunk.size.bytes")
  @Default("4*1024*1024")
  public final int routerMaxPutChunkSizeBytes;

  /**
   * The factory class to use to create the router.
   */
  @Config("router.factory")
  @Default("com.github.ambry.router.NonBlockingRouterFactory")
  public final String routerFactory;

  /**
   * Create a RouterConfig instance.
   * @param verifiableProperties the properties map to refer to.
   */
  public RouterConfig(VerifiableProperties verifiableProperties) {
    routerFactory =
        verifiableProperties.getString("router.factory", "com.github.ambry.router.NonBlockingRouterFactory");
    routerScalingUnitCount = verifiableProperties.getIntInRange("router.scaling.unit.count", 1, 0, 10);
    routerHostname = verifiableProperties.getString("router.hostname");
    routerDatacenterName = verifiableProperties.getString("router.datacenter.name");
    routerMaxConnectionsPerPortPlainText =
        verifiableProperties.getIntInRange("router.max.connections.per.port.plain.text", 5, 1, 20);
    routerMaxConnectionsPerPortSsl =
        verifiableProperties.getIntInRange("router.max.connections.per.port.ssl", 2, 1, 20);
    routerMaxPutChunkSizeBytes = verifiableProperties.getInt("router.max.chunk.size.bytes", 4 * 1024 * 1024);
  }
}
