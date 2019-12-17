package edu.hbuas.netdisk.config;
/**
 * 网盘配置类
 * @author tengsir
 *
 */
public interface NetDiskConfig {
	public String jdbcDriverClass="com.mysql.jdbc.Driver";
	public String jdbcURL="jdbc:mysql://localhost:3306/netdisk?useSSL=false";
	public String jdbcUsername="root";
	public String jdbcPassword="root";
	public String netDiskServerIP="localhost";
	public int netDiskServerPort=9999;

}
