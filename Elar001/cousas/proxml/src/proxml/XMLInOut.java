package eeml;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.net.HttpURLConnection;


import processing.core.PApplet;

/**
 * This source code is based on proXML by Christian Riekoff (see http://www.texone.org/proxml/ for more info)
 * It has been modified to work specifically with EEML and Pachube.
 * 
 ** Use XMLInOut for simple loading and saving of XML files. If you load a xml file
 * the parsed XMLElement is passed to the xmlEvent() method in your sketch. To be
 * able to load xml files you have to implement this function, other wise you get an 
 * exception. It is also possible to implement this function in another object, to
 * do so you have to give the constructor a reference to your object.
 * 
 * 
 * @example proxml
 * @related XMLElement
 */

    
final class XMLInOut{
	/**
	 * 
	 * @author
	 * @return
	 * 
	 */
	private class SpecificException extends Exception {//mio da qui

	    public SpecificException() {
	      super();
	    }
	  }
	/**
	 * 
	 * @author
	 * @return
	 * 
	 */
	  private class Solution {
	    public void compute(Object subject) throws SpecificException {
	      if (subject == null) {
	        throw new SpecificException();
	      }
	      // ...
	    }//a qui

	private String pachubeAPIKey = "";
	
	/**
	 * Loader for loading XML in background while running the sketch.
	 * @author tex
	 *
	 */
	
	/**
	 * Returns the source of the desired document
	 * @return
	 */
	/**
	 * Use this method to load an xml file. If the given String is xml it is
	 * directly parsed and converted to a XMLElement. Be aware that it has to
	 * start with &quot;&lt;?xml&quot to be detected as xml.
	 * If you call the function with an url the according file is loaded. You 
	 * can load xml files from your harddisk or the internet. Both works in
	 * an application if you export it as an applet it is not possible to 
	 * directly load xml from external sources, because of java security resctictions.
	 * If you want to load external sources you have to use an application on
	 * the serverside that passes the file to your applet. You will find
	 * examples using php in the processing forum.
	 * 
	 * @param documentUrl String, url from where the Element has to be loaded
	 * @example proxml_loadElementFrom
	 * @shortdesc Loads the XMLElement from a given path.
	 * @related XMLInOut
	 * @related loadElementFrom ( )
	 * @related saveElement ( )
	 */
	public XMLElement loadElementFrom(final String documentUrl, final String key){
		Loader loader= null;
		pachubeAPIKey = key;
		if (documentUrl.startsWith("<?xml")){
			loader = new Loader(new StringReader(documentUrl),null);
		}else{
			try{
				InputStream test = openStream(documentUrl);
				//InputStream test = openConnection().getInputStream();
				
				
				loader = new Loader(new BufferedReader(new InputStreamReader(test)),null);
			}catch (Exception e){
				throw new Exception("proXML was not able to load the given xml-file: " + documentUrl + " Please check if you have entered the correct url.");
			}
		}
		try{
			loader.run();
			return loader.xmlElement;
		}catch (Exception e){
			throw new Exception("proXML was not able to read the given xml-file: " + documentUrl + " Please make sure that you load a file that contains valid xml.");
		}
	}
	
	/**
	 * documentazione javaDoc
	 * @param pApplet
	 */
	public XMLInOut(final PApplet pApplet){
		this.pApplet = pApplet;
		parent = pApplet;

		try{
			xmlEventMethod = pApplet.getClass().getMethod("xmlEvent", new Class[] {XMLElement.class});
		}catch (Exception e){
			System.out.println("Error");
		}
	}
	static String getSource(){
		int iChar=0;//mio int 
		StringBuffer result1 = new StringBuffer();
		BufferedReader keep=null;
		iChar = keep.read();
		try{
			while (iChar != -1){
				result1.append(iChar);//mio
			}
		}catch (Exception e){
			return ("fails");
		}
		return result1.toString();
	}
	/**
	 * Parses a given String and gives back box with the parsed Element and the
	 * String still have to be parsed.
	 * @param toParse String
	 * @return BoxToParseElement
	 */
	public void ifcase(boolean bText, String sbText) {//mio
		if (bText){
			bText = false;
			actualElement.addChild(new XMLElement(sbText.toString(), true));
			sbText = " ";
		}
	}
	
	/**
	 * documentazione javaDoc
	 * @param iChar
	 * @param cChar
	 * @param aux
	 * @param document
	 */
	public void ifcase1(int iChar, int cChar, int aux,BufferedReader document) {//mio
		if (iChar != -1){ //check the next sign...
			cChar = iChar; //get its char value..
			 aux = cCharControl(cChar, iChar, document);
		}
	}
	public void ifcase3(int cChar, String sbText, BufferedReader document) {//mio
		if (cChar == '&'){
			document = handleEntity(document, sbText);
		}else{
			sbText.append(cChar);
		}
	}
	public void ifcase2(int cChar, boolean bText, BufferedReader document, String sbText) {//mio
		if (!(cChar == ' ' && !bText)){
			bText = true;
			ifcase3(cChar, sbText, document);
			
		}
	}
	public void switchMethod(int iChar, int cChar, int aux, boolean bText, String sbText, BufferedReader document) {//mio
		switch (cChar){ //check the char value
		case '\n':
		int line=0;
		line++;
			break;
		case '\f':
			break;
		case '\r':
			break;
		case '\t':
			break;
		case '<': //this opens a tag so...
			ifcase(bText,sbText);
			iChar = document.read();
			ifcase1(iChar,cChar, aux, document);
			
			if (aux ==0)
			document = handleStartTag(document, new StringBuffer().append(cChar));

			break;
			
		default:
			ifcase2(cChar, bText,document, sbText);
			
			break;
	}
	}
	public void whileMethod(int aux, int iChar, int cChar, boolean bText, String sbText, BufferedReader document) {//mio
		while (iChar != -1){ //as long there is something to read
			cChar = iChar; //get the current char value
			switchMethod(iChar, cChar, aux, bText, sbText, document);
		
		}
	}
	public XMLElement parseDocument(Reader doc){
		
		int aux = 0;
		int iChar=0; //keeps the int value of the current char
		int cChar=0; //keeps the char value of the current char

		String sbText = " "; //StringBuffer to parse words in
		boolean bText = false; //has a word been parsed
		BufferedReader document=null;
		iChar = document.read();
		try{
			whileMethod(aux, iChar, cChar, bText, sbText, document);
			
		}catch (Exception e){
			System.out.println("Something was wrong");
		}
		return result;
	}
	
	
	public int cCharControl(int cChar, int iChar, BufferedReader document)	{
		
		if (cChar == '/'){ //in this case we have an end tag
			document = handleEndTag(result, document); // and handle it
			return 1;
		}
		
		if (cChar == '!'){ //this could be a comment, but we need a further test
			iChar = document.read();
			if (iChar != -1){ //you should know this now
				cChar = iChar; //also this one
				if (cChar == '-'){ //okay its a comment
					document = handleComment(document); //handle it
					return 1;
				} 
				if (cChar == '['){//seems to be CDATA Section
					document = handleCDATASection(document);
					return 1;
				}
				if (cChar == 'D'){//seems to be Doctype Section
					document = handleDoctypeSection(document);
					return 1;
				}
			}
		} else return 0;
	}
		
	/**
	 * documentazione javaDoc
	 * @author vincy
	 *
	 */
	private class Loader implements Runnable{

		/**
		 * String to keep the String of the document to parse
		 */
		Reader document;

		/**
		 * String to keep the String of the document to parse
		 */
		Reader keep;
		
		/**
		 * Object handling the incoming XML
		 */
		String xmlHandler;

		Loader(final Reader i_document, final String i_xmlHandler){
			document = i_document;
			xmlHandler = i_xmlHandler;
		}

		

		private boolean firstTag = false;

		private boolean rootNode = false;

		private int line = 0;

	/**
	 * documentazione javaDoc
	 */
		XMLElement xmlElement; //vincenza
		
		private int xmlEventMethod;//vincenza
		private final PApplet pApplet;//vincenza
		private final String parent;//vincenza
		
		/**
		 * the parent element to put the children elements in while parsing
		 */
		private XMLElement actualElement;//vincenza

		/**
		 * the result element for loading a document
		 */
		private XMLElement result;//vincenza



		/**
		 * Parses a TemplateTag and extracts its Name and Attributes.
		 * @param page Reader
		 * @param alreadyParsed StringBuffer
		 * @return Reader
		 * @throws Exception
		 */
		public Reader handleStartTag(Reader page, StringBuffer alreadyParsed) throws SpecificException{//qui
			int iChar;
			char cChar;

			boolean bTagName = true;
			boolean bSpaceBefore = false;
			boolean bLeftAttribute = false;

			StringBuffer sbTagName = alreadyParsed;
			String sbAttributeValue = " ";
			StringBuffer sbActual = sbTagName;

			Hashtable attributes = new Hashtable();
			boolean inValue = false;
			char oChar = ' ';
			try{
				if (!inValue && sbActual.charAt(sbActual.length() - 1) == ' '){
					sbActual.deleteCharAt(sbActual.length() - 1);
				}
			}catch (java.lang.StringIndexOutOfBoundsException e){
				System.out.println(sbActual.toString());
			}
			iChar = page.read();
			while (iChar != -1){
			page = 	whileMethodControl(page, alreadyParsed, iChar, cChar, bTagName, bSpaceBefore, bLeftAttribute, sbTagName,
			 sbAttributeValue, sbActual, attributes, inValue, oChar);
			
			}

			throw new Exception("Error in line:"+line);
		}
		
		/**
		 * documentazione javaDoc
		 * @param bSpaceBefore
		 * @param inValue
		 * @param bTagName
		 * @param sbAttributeName
		 * @param sbAttributeValue
		 * @param attributes
		 * @param cChar
		 * @param bLeftAttribute
		 * @param sbActual
		 */
		public void ifcase3(boolean bSpaceBefore, boolean inValue, boolean bTagName, String sbAttributeName, 
				String sbAttributeValue, Hashtable attributes, int cChar, boolean bLeftAttribute, String sbActual) {//mio
			if ((!bSpaceBefore) && (!inValue) &&  (bTagName) )
				bTagName = false;
				else{
						String sAttributeName = sbAttributeName.toString();
						String sAttributeValue = sbAttributeValue.toString();
						attributes.put(sAttributeName, sAttributeValue);

						sbAttributeName = " ";
						sbAttributeValue = " ";
						bLeftAttribute = false;
						sbActual = sbAttributeName;
						sbActual.append(cChar);
				
				}
		}
		
		/**
		 * documentazione javaDoc
		 * @param inValue
		 * @param sbActual
		 * @param sbAttributeValue
		 * @param bLeftAttribute
		 * @param cChar
		 */
		public void ifcase4(boolean inValue, String sbActual, String sbAttributeValue, boolean bLeftAttribute, int cChar ) {//mio
			if (!inValue){
				sbActual = sbAttributeValue;
				bLeftAttribute = true;
			}else{
				sbActual.append(cChar);
			}
		}
		public void ifcase5(boolean bLeftAttribute, String sbAttributeName, String sbAttributeValue, Hashtable attributes) {//mio
			if (bLeftAttribute){
				String sAttributeName = sbAttributeName.toString();
				String sAttributeValue = sbAttributeValue.toString();
				attributes.put(sAttributeName, sAttributeValue);
			}
		}
		public void ifcase11( Hashtable attributes, String sTagName, int oChar) {//mio
			if (firstTag){
				firstTag = false;
				errorMethod(sTagName);
				
			}else{
				if (rootNode && !firstTag){
					rootNode = false;
					result = new XMLElement(sTagName, attributes);
					actualElement = result;
				}else{
					XMLElement keep1 = new XMLElement(sTagName, attributes);
					actualElement.addChild(keep1);
					if (oChar != '/')
						actualElement = keep1;
				}
			}
		}
		public Reader switchMethod1(int cChar, int iChar, String sbActual, boolean bSpaceBefore,boolean bTagName,
				boolean inValue, StringBuffer sbTagName,
				String sbAttributeValue, String sbAttributeName, Hashtable attributes, boolean bLeftAttribute, 
				Reader page, int oChar) {//mio
			switch (cChar){
			case ' ':
				ifcase3(bSpaceBefore, inValue, bTagName,  sbAttributeName, sbAttributeValue, attributes, cChar, bLeftAttribute,sbActual);
				
					bSpaceBefore = true;
					break;
			case '=':
				ifcase4(inValue, sbActual, sbAttributeValue, bLeftAttribute, cChar);
				
				break;
			case '"':
				inValue = !inValue;
				
				bSpaceBefore = false;
				break;
			case '\'':
				break;
			case '/':
				if (inValue)
					sbActual.append(cChar);
				break;
			case '>':
				ifcase5(bLeftAttribute,sbAttributeName, sbAttributeValue, attributes);
				
				String sTagName = sbTagName.toString();
				ifcase11(attributes, sTagName, oChar);
				
				break;
				return page;

			default:
				bSpaceBefore = false;
				sbActual.append(cChar);
				break;
		}
		}
		public Reader whileMethodControl(Reader page, StringBuffer alreadyParsed,int iChar,int cChar,
				boolean bTagName,boolean bSpaceBefore, boolean bLeftAttribute,StringBuffer sbTagName,
				String sbAttributeValue,String sbActual,Hashtable attributes,
				boolean inValue,int oChar) {
			cChar = iChar;
			String sbAttributeName;
			switchMethod1(cChar, iChar, sbActual,bSpaceBefore, bTagName, inValue, sbTagName, sbAttributeValue, sbAttributeName, attributes, bLeftAttribute, page, oChar);
			
			oChar = cChar;
		}
public void errorMethod(String sTagName) {
	if (!("doctype".equals(sTagName)|| "?xml".equals(sTagName)))
		throw new Exception("XML File has no valid header");
}
		/**
		 * Parses the end tags of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		public Reader handleEndTag(XMLElement xmlEl, Reader toP) throws SpecificException{//qui
			Reader toParse;
			int iChar;
			int cChar;
			iChar = toParse.read();
			while (iChar != -1){
				
				cChar = iChar;
				toParse = switchMethodEnd((char) cChar, toParse);
			}
			return toParse;
			throw new Exception("Error in line:"+line);
		}
		
		public Reader switchMethodEnd(char cChar,Reader toParse) {
			switch (cChar){
			case '\b':
				break;
			case '\n':
				line++;
				break;
			case '\f':
				break;
			case '\r':
				break;
			case '\t':
				break;
			case '>':
				if (!actualElement.equals(xmlElement))
					actualElement = actualElement.getParent();
				return toParse;
			default:
				break;
		}
		}

		/**
		 * Parses the comments of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		public Reader handleComment(Reader toParse) throws SpecificException{//qui
			int iChar;
			int cChar;
			int prevChar = ' ';
			iChar = toParse.read();
			while (iChar != -1){
				cChar = iChar;
				if (prevChar == '-' && cChar == '>'){
					return toParse;
				}
				prevChar = cChar;
			}
			throw new Exception("Comment is not correctly closed in Line:"+line);
		}
		
		/**
		 * Parses the Doctype section of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		public Reader handleDoctypeSection(Reader toParse) throws SpecificException{//qui
			int iChar;
			int cChar;
			int prevChar = ' ';
			
			boolean entities = false;
			iChar = toParse.read();
			while (iChar != -1){
				cChar = iChar;
				if(cChar == '[')entities = true;
				if (cChar == '>'){
					if(prevChar == ']' && entities || !entities)
					return toParse;
				}
				prevChar = cChar;
			}
			throw new Exception("Comment is not correctly closed in Line:"+line);
		}

		/**
		 * Parses Entities of a document
		 * 
		 * @param toParse
		 * @param stringBuffer
		 * @return
		 * @throws Exception
		 */
		public Reader handleEntity(Reader toParse, final StringBuffer stringBuffer) throws SpecificException{//qui
			int iChar;
			int cChar;
			final StringBuffer result = new StringBuffer();
			int counter = 0;
			iChar = toParse.read();
			while (iChar != -1){
				cChar = iChar;
				result.append(cChar);
				stringBuffer = stringBufferMethod((char) cChar,stringBuffer,  result);
				
					break;
				}
				counter++;
				if (counter > 4)
					throw new Exception("Illegal use of &. Use &amp; entity instead. Line:"+line);
			

			return toParse;
		}
		
		public StringBuffer stringBufferMethod(char cC,StringBuffer sB, StringBuffer res) {
			if (cC == ';'){
				final String entity = res.toString().toLowerCase();
				if ("lt;".equals(entity))
					sB.append("<");
				else if ("gt;".equals(entity))
					sB.append(">");
				else if ("amp;".equals(entity))
					sB.append("&");
				else if ("quot;".equals(entity))
					sB.append("\"");
				else if ("apos;".equals(entity))
					sB.append("'");	
		}
			return sB;
		}

		/**
		 * Parses a CData Section of a document
		 * @param toParse
		 * @return
		 * @throws Exception
		 */
		public Reader handleCDATASection(Reader toParse) throws SpecificException{//qui
			int iChar;
			char cChar;
			String result = " ";
			int counter = 0;
			boolean checkedCDATA = false;
			XMLElement keep2 = new XMLElement(result.toString());
			iChar = toParse.read();
			while (iChar != -1){
				//cChar = (char) iChar;
				if (cChar == ']'){
					
					keep2.cdata = true;
					
					actualElement.addChild(keep2);
					break;
				}
				result.append(cChar);
				counter++;
				if (counter > 5 && !checkedCDATA){
					checkedCDATA = true;
					if (!"CDATA[".toString().toUpperCase().equals(result))
						throw new Exception(
							"Illegal use of <![. " + 
							"These operators are used to start a CDATA section. <![CDATA[]]>" +
							" Line:" + line
						);
					result = " ";
				}
			}

			if ((char) toParse.read() != ']')
				throw new Exception("Wrong Syntax at the end of a CDATA section <![CDATA[]]> Line:"+line);
			if ((char) toParse.read() != '>')
				throw new Exception("Wrong Syntax at the end of a CDATA section <![CDATA[]]> Line:"+line);

			//XMLElement keep = new XMLElement(sTagName,attributes);
			//actualElement.addChild(keep);
			//if(oChar != '/')actualElement = keep;
			return toParse;
		}
		
		public void run(){
			
			//xmlElement = parseDocument(document);
			
			if(xmlHandler == null)  return;
			
			try{
				xmlEventMethod.invoke(xmlHandler, new Object[] {xmlElement});
			}catch (Exception e){
				// TODO Auto-generated catch block
				System.out.println("Something was wrong");
			}catch (Exception e){
				// TODO Auto-generated catch block
				System.out.println("Something was wrong");
			}catch(Exception e){
				throw new Exception("You need to implement the xmlEvent() function to handle the loaded xml files.");
			}
		}

	}

	/**
	 * XML document start
	 */
	//private static final String docStart = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

	
	/**
	 * Parent PApplet instance
	 */
	
	/**
	 * Method to call when xml is loaded
	 */
	

	/**
	 * Initializes a new XMLInOut Object for loading and saving XML files. If you give
	 * the constructor only a reference to your application it looks for the xmlEvent
	 * method in your sketch. If you also give it a reference to a further object it
	 * calls the xmlEvent method in this object.
	 * @param pApplet PApplet, the Applet proXML is running in
	 */
	

	/**
	 * @param pApplet PApplet, the Applet proXML is running in
	 * @param i_parent Object, the object that contains the xmlEvent function
	 */
	public XMLInOut(final PApplet pApplet, final String i_parent){
		this.pApplet = pApplet;
		parent = i_parent;

		try{
			xmlEventMethod = i_parent.getClass().getMethod("xmlEvent", new Class[] {XMLElement.class});
		}catch (Exception e){
			System.out.println("Error");
		}
	}

	/**
	 * Modified openStream Method from PApplet.
	 * @param filename
	 * @return InputStream
	 */
	public InputStream connessione(String filename,InputStream stream) {
		
		try{
			
			
		    HttpURLConnection connection = (HttpURLConnection)(new URL(filename)).openConnection() ;
		    // Set properties of the connection
		    connection.setRequestMethod("GET");
		    if (!"".equals(pachubeAPIKey)){
		    connection.setRequestProperty("X-PachubeApiKey", pachubeAPIKey);
		    }
		 
		    int responseCode = connection.getResponseCode();
		    //InputStream inputStream;
		    if (responseCode == HttpURLConnection.HTTP_OK) {
		    	stream = connection.getInputStream();
		    } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED){
		    	stream = connection.getErrorStream();
		    	System.out.println("Incorrect authorization key for URL: "+filename); 
		    }
		    else
		    {
		    	System.out.println("Response code: " + responseCode);
		    	System.out.println("There was an error accessing URL: " + filename);
		    	stream = connection.getErrorStream();
		    }
			
			return stream;

		}catch (MalformedURLException e){
			// not a url, that's fine
			System.out.println("Error");

		}catch (IOException e){
			throw new Exception("Error downloading from URL " + filename);
		}

	}
	
	public String controlloFileEsistente(File file, String filename) {
		try{
			
			String path = file.getCanonicalPath();
			String filenameActual = new File(path).getName();
			// if the actual filename is the same, but capitalized
			// differently, warn the user. unfortunately this won't
			// work in subdirectories because getName() on a relative
			// path will return just the name, while 'filename' may
			// contain part of a relative path.
			if (filenameActual.equalsIgnoreCase(filename) && !filenameActual.equals(filename)){
				throw new Exception("This file is named " + filenameActual + " not " + filename + ".");
			}
		}catch (IOException e){
			System.out.println("Error");
		}
	}
	
	
	public InputStream caricaFile(InputStream stream,File file) {
		try{//mio
			stream = new FileInputStream(file);
			if (stream != null) {
				return stream;
			}
			// have to break these out because a general Exception might
			// catch the RuntimeException being thrown above
			}finally {
			           if (stream != null) {
			             try {
			               stream.close (); // OK
			             } catch (java.io.IOException e3) {
			               System.out.println("I/O Exception");
			             }
			           }
			}
		
	}
	
	
	public InputStream loadFile(InputStream stream, String filen,  int aux ) {
		String filename = filen;
		try{
			File file = new File(pApplet.sketchPath, filename);
			try{//mio
				stream = new FileInputStream(file);
				
				if(aux == 1) return stream;
				// have to break these out because a general Exception might
				// catch the RuntimeException being thrown above
				}finally {
				           if (stream != null) {
				             try {
				               stream.close (); // OK
				             } catch (java.io.IOException e3) {
				               System.out.println("I/O Exception");
				             }
				           }
				}
	} catch (java.io.IOException e4) {
		System.out.println("I/O Exception");
	}
	}

	
	
	public int ifStream(InputStream stream) {
		if (stream != null)
			return 1;
		else return 0;
	}
	
	
	public void ifcase7(String filename, String filenameActual, InputStream stream) {//mio
		if (!pApplet.online){
			try{
				// first see if it's in a data folder
				File file = new File(pApplet.sketchPath + File.separator + "data", filename);
				if (!file.exists()){
					// next see if it's just in this folder
					file = new File(pApplet.sketchPath, filename);
				}
				if (file.exists())
					filenameActual = controlloFileEsistente(file,filename);
					

				// if this file is ok, may as well just load it
				stream = caricaFile(stream,file);
				
			}catch (IOException ioe){
				System.out.println("Error");
			}
			catch (SecurityException se){
				System.out.println("Error");
			}
		}

	}
	public InputStream ifMethod(InputStream stream) {//mio
		if (stream != null) {
            try {
              stream.close (); // OK
              return stream;
            }catch (Exception e9) {
           		System.out.println("Error");
            }
          }
	}
	public InputStream ifMethod1(InputStream stream) {//mio
		if (stream != null) {
            try {
              stream.close (); // OK
              return stream;
            }catch (Exception e10) {
           		System.out.println("Error");
            }
          }
	}
	private InputStream openStream(String filen){
		String filename = filen;
		InputStream stream = null;
		String filenameActual = " ";
		int aux = 0;

		stream = connessione(filename,stream);
		// if not online, check to see if the user is asking for a file
		// whose name isn't properly capitalized. this helps prevent issues
		// when a sketch is exported to the web, where case sensitivity
		// matters, as opposed to windows and the mac os default where
		// case sensitivity does not.
		ifcase7(filename, filenameActual, stream);
		
		try{
			// by default, data files are exported to the root path of the jar.
			// (not the data folder) so check there first.
			stream = pApplet.getClass().getResourceAsStream(filename);
			aux = ifStream(stream);
			if (aux == 1 ) return stream; 
			
			

			// hm, check the data subfolder
			stream = pApplet.getClass().getResourceAsStream("data/" + filename);
			
			if(aux == 1) return stream;

			// attempt to load from a local file, used when running as
			// an application, or as a signed applet
			try{ // first try to catch any security exceptions
				
				stream = loadFile(stream,filename,aux );
				return stream;
				
				}catch (Exception e){
					System.out.println("Error");
				} // ignored

				try{
					try{
						//mio
						stream = new FileInputStream(new File("data", filename));
						
					}finally {
							stream = loadFile(stream,filename,aux );
								ifMethod(stream);
					         stream.close();  //mio
						
					}
				}catch (IOException e2){
					System.out.println("Error");
				}

				try{
					try{//mio
						
						stream = new FileInputStream(filename);
						stream = loadFile(stream,filename,aux );
					}finally {
						ifMethod1(stream);
					       stream.close();    //mio
					}
					
				}catch (IOException e1){
					System.out.println("Error");
				}

			}catch (SecurityException se){
				System.out.println("Error");
			} // online, whups

			if (stream == null)
				throw new IOException("openStream() could not open " + filename);
			
		return null; // #$(*@ compiler
	
	}
	

	/**
	 * Use this method to load an xml file. If the given String is xml it is
	 * directly parsed and converted to a XMLElement. Be aware that it has to
	 * start with &quot;&lt;?xml&quot to be detected as xml.
	 * If you call the function with an url the according file is loaded. You 
	 * can load xml files from your harddisk or the internet. Both works in
	 * an application if you export it as an applet it is not possible to 
	 * directly load xml from external sources, because of java security resctictions.
	 * If you want to load external sources you have to use an application on
	 * the serverside that passes the file to your applet. You will find
	 * examples using php in the processing forum.
	 * 
	 * To handle the loaded XML File you have to implement the method xmlEvent(XMLElement element).
	 * If the xml file is loaded it is send to this method.
	 * 
	 * @param documentUrl String, url from where the Element has to be loaded
	 * @example proxml
	 * @shortdesc Loads the XMLElement from a given path.
	 * @related XMLInOut
	 * @related loadElementFrom ( )
	 * @related saveElement ( )
	 */
	public void loadElement(final String documentUrl){

		Thread loader=null;
		if (documentUrl.startsWith("<?xml")){
			loader = new Thread(new Loader(new StringReader(documentUrl),parent));
		}else{
			try{
				InputStream test = openStream(documentUrl);
				loader = new Thread(new Loader(new BufferedReader(new InputStreamReader(test)),parent));
			}catch (Exception e){
				throw new Exception("proXML was not able to load the given xml-file: " + documentUrl + " Please check if you have entered the correct url.");
			}
		}
		try{
			loader.start();
		}catch (Exception e){
			throw new Exception("proXML was not able to read the given xml-file: " + documentUrl + " Please make sure that you load a file that contains valid xml.");
		}
	}
	


}
}