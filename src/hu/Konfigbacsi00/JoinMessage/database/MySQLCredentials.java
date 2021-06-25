package hu.Konfigbacsi00.JoinMessage.database;

public class MySQLCredentials {
	
	private String host;
    private int port;
    private String username;
    private String password;
    private String db;
    private String table;
    
    public MySQLCredentials(String host, int port, String username, String password, String db, String table){
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
        this.db = db;
        this.table = table;
    }
    
    public String getHost(){
        return host;
    }
    
    public int getPort(){
        return port;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public String getDatabase(){
        return db;
    }
    
    public String getTable(){
    	return table;
    }

}
