package eeml;

//v.1.01 (November 2008)
//EEML (Extended Environments Markup Language) see http://www.eeml.org/ for more info

//The EEML library facilitates the sharing of EEML documents between remote environments via
//a network. It is used particularly in connecting to the main repository for feeds 
//located at http://www.pachube.com/
//The EEML library makes use of the existing processing net library
//XML parsing is based on proXML by Christian Riekoff (see http://www.texone.org/proxml/ for more info)
//thank you! 


import processing.core.PApplet;
import java.net.HttpURLConnection;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;



/**
 * This is one of the basic classes but should not normally be used, since it's not threaded.
 * Instead see the DataOut class. 
 * @see DataOut
 */


public class Out {

	/**
	 * dichiarazione 
	 * documetazione javaDoc
	 */
	PApplet parent;
	private XMLElement localEnvironment;	
	private Server myServer;
	private String incomingMsg;

	private final String HTTPHeader = "HTTP/1.1 200 OK\n";
	private final String contentHeader = "Content-type: application/xml\n\n";
	private final String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
	private final String eemlHeader = "<eeml xmlns=\"http://www.eeml.org/xsd/005\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.eeml.org/xsd/005 http://www.eeml.org/xsd/005/005.xsd\" version=\"5\">\n";
	private final String eemlFooter = "\n</eeml>";

	private String locationData = "";
	
	private XMLElement valueChild;
	private XMLElement unitChild;

	//private XMLElement dataElement;
	private XMLElement valueElement;
	//private XMLElement tagElement;
	
	/**
	 * 
	 * @param parent
	 * @param myport
	 */
	public Out (PApplet parent, int myport) { 

		this.parent = parent;
		myServer = new Server(parent, myport);
		localEnvironment = new XMLElement("environment");
		incomingMsg = null;		
		
		//dataElement = new XMLElement("data");
		valueElement = new XMLElement("value");
		//tagElement = new XMLElement("tag");
	} 

	/**
	 * metodo publico out
	 * documentazione javaDoc
	 * @param parent
	 */
	public Out (PApplet parent) { 

		localEnvironment = new XMLElement("environment");
		incomingMsg = null;		
		
		valueElement = new XMLElement("value");
	} 

/**
 * metofo per l'aggiunta della data
 * documetazione javaDoc
 * @param id
 * @param tags
 */
	public void addData(int id, String tags){		
		
		XMLElement thisDatastream = new XMLElement("data");   
		thisDatastream.addAttribute("id",Integer.toString(id));
		String[] thisDatastreamTags =  tags.split(","); 
		int thisDatastreamTagCount = thisDatastreamTags.length;
		XMLElement tag = new XMLElement("tag");
		XMLElement tagChild = new XMLElement(thisDatastreamTags[i], true);
		
		for (int i = 0; i < thisDatastreamTagCount ; i++)
		{
			tag.addChild(tagChild);
			thisDatastream.addChild(tag);
		}

		valueChild = new XMLElement("value");		
		XMLElement valueChildChild = new XMLElement("",true);
		valueChild.addChild(valueChildChild);
		thisDatastream.addChild(valueChild);				

		unitChild = new XMLElement("unit");		
		XMLElement unitChildChild = new XMLElement(" ",true);
		unitChild.addChild(unitChildChild);
		thisDatastream.addChild(unitChild);				

		localEnvironment.addChild(thisDatastream);
		
	}

	public void addData(int id, String tags, float minimum, float maximum){		
		
	this.addData(id,tags);
	this.setMinimum(id,minimum);
	this.setMaximum(id,maximum);
		
	}

	public void setLocation(String exposure, String domain, String disposition, float lat, float lon, float ele){
		locationData = "  <location exposure=\""+ exposure + "\" domain=\""+domain+"\" disposition=\""+disposition+"\">\n    <lat>"+lat+"</lat>\n    <lon>"+lon+"</lon>\n    <ele>"+ele+"</ele>\n  </location>\n";
	}

	public void setLocation(String exposure, String domain, String disposition){
		locationData = "  <location exposure=\""+ exposure + "\" domain=\""+domain+"\" disposition=\""+disposition+"\">\n    </location>\n";
	}

	public void setLocation(float lat, float lon, float ele){
		locationData = "  <location>\n    <lat>"+lat+"</lat>\n    <lon>"+lon+"</lon>\n    <ele>"+ele+"</ele>\n  </location>\n";

	}

	public void setMinimum(int id, float minimum){
		localEnvironment.getChild(id).getChild(localEnvironment.getChild(id).countChildren()-2).addAttribute("minValue",minimum);
	}

	public void setMaximum(int id, float maximum){
		localEnvironment.getChild(id).getChild(localEnvironment.getChild(id).countChildren()-2).addAttribute("maxValue",maximum);
	}


	public void setUnits(int id, String unit_, String symbol_, String type_){
		XMLElement units = new XMLElement("unit");
		units.addAttribute("symbol", symbol_);
		units.addAttribute("type",type_);
		units.addChild(new XMLElement(unit_, true));
		localEnvironment.getChild(id).removeChild(localEnvironment.getChild(id).countChildren()-1);
		localEnvironment.getChild(id).addChild(units);
	}


	public void update(int id, float value){
		XMLElement val = localEnvironment.getChild(id).getChild(localEnvironment.getChild(id).countChildren()-2);
		if (isValueElement(val)){
		val.removeChild(0);
		val.addChild(new XMLElement(Float.toString(value), true),0);
		}
	}

	public void update(int id, String value){
		XMLElement val = localEnvironment.getChild(id).getChild(localEnvironment.getChild(id).countChildren()-2);
		if (isValueElement(val)){
		val.removeChild(0);
		val.addChild(new XMLElement(value, true),0);
		}
	}

	public void printXML() {
		localEnvironment.printElementTree(); 
	}

	public String serve() {

		Client thisClient = myServer.available();

		if (thisClient != null ) {

			incomingMsg = null;

			String servedXML = createEEML();
			
			myServer.write(HTTPHeader + contentHeader + servedXML);

			//next few lines should contain the IP address of the device sniffing your XML

			incomingMsg = thisClient.readString(); 

			//System.out.println("actualoutput: " + servedXML);    

			myServer.disconnect(thisClient);

		}

		return incomingMsg;

	}
/**
 * metodo per la creazione di EEML
 * documentazione JavaDoc
 * @return
 */
	private String createEEML(){
		
		int totalDatastreams = localEnvironment.countChildren();
		String servedXML = xmlHeader + eemlHeader + localEnvironment + "\n" + locationData;
		
		for (int i = 0; i < totalDatastreams; i++) {

			XMLElement thisStream = localEnvironment.getChild(i);

			//servedXML += "  " + thisStream + "\n";
			servedXML = String.concat("  " + thisStream + "\n");//mio


			int totalTags = thisStream.countChildren() - 2;

			for (int j = 0; j < totalTags; j++) {

				
				servedXML = String.concat("    <tag>" + thisStream.getChild(j).getChild(0).toString().trim() + "</tag>\n");//mio
			}


			
			servedXML = String.concat("    " + thisStream.getChild(totalTags));//mio

			servedXML = String.concat( thisStream.getChild(totalTags).getChild(0));//mio
			servedXML = servedXML + "</value>\n";
			


			
			servedXML = String.concat( "    " + thisStream.getChild(totalTags+1));//mio
			
			servedXML =String.concat(thisStream.getChild(totalTags+1).getChild(0));//mio
			servedXML = servedXML + "</unit>\n";
			
			servedXML = servedXML + "  " + "</data>" + "\n";
			
		}

		servedXML += "</environment>"+eemlFooter;

		return servedXML;
		
	}
	
	
	public String serverMessage(){
		return incomingMsg;
	}

	boolean hasClient(){

		boolean hasClient_ = false;	
		Client thisClient = myServer.available();
		if (thisClient != null ) { 
			hasClient_ = true; 
		}	
		return hasClient_;
	}

	/**
	 * documentazione javaDoc
	 * @param thisElement
	 * @return
	 */
	private boolean isValueElement(XMLElement thisElement){

		boolean is = false;
		if (thisElement.getElement().equals(valueElement.getElement())) {
			is = true;	
		}
		return is;
	}
	
	public int updatePachube(String updateURL, String pachubeAPIKey){		
		int rCode = 999;

		try {
			rCode = updateDataAuthorizer(updateURL,createEEML(), pachubeAPIKey);
		}
		catch (Exception e)
		{
			System.out.println("Something was wrong"); 
		}
				
		return rCode;
	}

	public static Object updateDataAuthorizer(String urlstr, String parameters, String pachubeAPIKey)
	throws IOException
	{
		int rCode=0; 
		try{
		    
			URLConnection hpcon = new URL (urlstr).openConnection();            
			((HttpURLConnection) hpcon).setRequestMethod("PUT");
			hpcon.setRequestProperty("Content-Length", "" + Integer.toString(parameters.getBytes().length));
			hpcon.setRequestProperty ("X-PachubeApiKey", pachubeAPIKey);        

			
			hpcon.setRequestProperty("Content-Language", "en-US");  
			hpcon.setRequestProperty("Content-Type", "text/xml");
			hpcon.setUseCaches (false);
			hpcon.setDoInput(true);
			hpcon.setDoOutput(true);
			DataOutputStream printout = new DataOutputStream (hpcon.getOutputStream ());
			printout.writeBytes (parameters);
			printout.flush ();
			printout.close ();

			rCode = ((HttpURLConnection) hpcon).getResponseCode();
			return hpcon.getContent();		
		} catch(Exception  e){
			 logSecurityIssue(e); //
		      terminateInsecureConnection();
			System.out.println("There was a problem while updating the Pachube resource: " + e);
			throw new IOException("Something was wrong");	
			
		} finally {

			if(hpcon != null){
				hpcon.disconnect();	
			}
		}
		return rCode;
	}


}