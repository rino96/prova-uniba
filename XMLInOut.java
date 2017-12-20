//mio
package proxml;

//ghol

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import processing.core.PApplet;

/**
 * Use XMLInOut for simple loading and saving of XML files. If you load a xml file
 * the parsed XMLElement is passed to the xmlEvent() method in your sketch. To be
 * able to load xml files you have to implement this function, other wise you get an 
 * exception. It is also possible to implement this function in another object, to
 * do so you have to give the constructor a reference to your object.
 * 
 * 
 * @example proxml
 * @related XMLElement
 */
public class XMLInOut{

	/**
	 * Loader for loading XML in background while running the sketch.
	 * @author tex
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
		Object xmlHandler;

		Loader(final Reader i_document, final Object i_xmlHandler){
			document = i_document;
			xmlHandler = i_xmlHandler;
		}

		/**
		 * Returns the source of the desired document
		 * @return
		 */
		String getSource(){
			int iChar;
			StringBuffer result = new StringBuffer();
			try{
				int read = keep.read();//mio
				while ((iChar = read) != -1){
					result.append(' ');
				}
			}catch (Exception e){
				return ("fails");
			}
			return result.toString();
		}

		private boolean firstTag = false;

		private boolean rootNode = false;

		private int line = 0;

		/**
		 * Parses a given String and gives back box with the parsed Element and the
		 * String still have to be parsed.
		 * @param toParse String
		 * @return BoxToParseElement
		 */
		
		//_____________________________________________________________________________________
		
		public void iCharReadMethod(int iChar, char cChar, StringBuffer sbText, boolean bText) {
			int read1= document.read();//mio
			while ((iChar = read1) != -1){ //as long there is something to read
				cChar = ' '; //get the current char value
				
				cCharSwitchMethod(cChar, sbText, bText, iChar);
			}
		}
		
		public void cCharSwitchMethod(char cChar, StringBuffer sbText, boolean bText, int iChar) {
			switch (cChar){ //check the char value
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
			case '<': //this opens a tag so...
				
				bTextMethod(bText, sbText);
				
				iCharCheckMethod(iChar, cChar);
				
				document = handleStartTag(document, new StringBuffer().append(cChar));

				break;
			default:
				
				defaultSwitchMethod(cChar, sbText, bText);
				break;
			}
		}
		
		public void bTextMethod(boolean bText, StringBuffer sbText) {
			if (bText){
				bText = false;
				actualElement.addChild(new XMLElement(sbText.toString(), true));
				sbText = new StringBuffer();
			}
		}
		
		public void iCharCheckMethod(int iChar, char cChar) {
			if ((iChar = document.read()) != -1){ //check the next sign...
				cChar = ' '; //get its char value..

				closeTagMethod(iChar, cChar);
				
		}
		}
		public void defaultSwitchMethod(char cChar, StringBuffer sbText, boolean bText) {
			if (!(cChar == ' ' && !bText)){
				bText = true;
				if (cChar == '&'){
					document = handleEntity(document, sbText);
				}else{
					sbText.append(cChar);
				}
			}
		}
		
		public void closeTagMethod(int iChar, char cChar) {
			if (cChar == '/'){ //in this case we have an end tag
				document = handleEndTag(result, document); // and handle it
				break;
			}else if (cChar == '!'){ //this could be a comment, but we need a further test
				
				ifShouldMethod(iChar, cChar);
				
			}			
		}
		
		public void ifShouldMethod(int iChar, char cChar) {
			if ((iChar = document.read()) != -1){ //you should know this now
				cChar = ' '; //also this one
				
				ifcCharMethod(cChar);
				
			}
			
		}
		
		public void ifcCharMethod(char cChar) {
			if (cChar == '-'){ //okay its a comment
				document = handleComment(document); //handle it
				break;
			}else if (cChar == '['){//seems to be CDATA Section
				document = handleCDATASection(document);
				break;
			}else if (cChar == 'D'){//seems to be Doctype Section
				document = handleDoctypeSection(document);
				break;
			}
		}
		
	//________________________________________________________________________________________
		
		private XMLElement parseDocument(Reader document){

			firstTag = true;
			rootNode = true;

			int iChar; //keeps the int value of the current char
			char cChar; //keeps the char value of the current char

			StringBuffer sbText = new StringBuffer(); //StringBuffer to parse words in
			boolean bText = false; //has a word been parsed
			try{
				
				iCharReadMethod(iChar, cChar, sbText, bText);
				
			}catch (Exception e){
				System.out.println("Something was wrong");
			}
			return result;
		}

		//___________________________________________________________________
		
		public void switchcCharMethod(char cChar, boolean bTagName, boolean bSpaceBefore, boolean bLeftAttribute, StringBuffer sbTagName, StringBuffer sbAttributeName, 
				StringBuffer sbAttributeValue, StringBuffer sbActual, Hashtable attributes, boolean inValue, char oChar) {

			switch (cChar){
			
			
			case ' ':
				ifCasebSpaceMethod(cChar, bTagName, bSpaceBefore, bLeftAttribute, sbAttributeName, sbAttributeValue, sbActual, attributes, inValue, oChar);
				
				bSpaceBefore = true;
				break;
			case '=':
				ifCaseInValueMethod(cChar, inValue, sbActual, sbAttributeValue, bLeftAttribute);
				
				break;
			case '"':
				inValue = !inValue;
				
				tryCaseMethod(inValue, sbActual);
				
				bSpaceBefore = false;
				break;
			case '\'':
				break;
			case '/':
				
				ifInValueMethod2(inValue, sbActual, cChar);
				
				break;
			case '>':
				ifBLeftAttributeCaseMethod(sbTagName, bLeftAttribute, attributes, sbAttributeName, oChar, sbAttributeValue);
				
				return page;

			default:
				bSpaceBefore = false;
				sbActual.append(cChar);
				break;
		}
		
		}
		
		public void ifCasebSpaceMethod(char cChar, boolean bTagName, boolean bSpaceBefore, boolean bLeftAttribute, StringBuffer sbAttributeName, 
				StringBuffer sbAttributeValue, StringBuffer sbActual, Hashtable attributes, boolean inValue, char oChar) {
			if (!bSpaceBefore) {
			
				ifInValueMethod(attributes, inValue, bTagName, sbActual, bLeftAttribute, sbAttributeName, sbAttributeValue, cChar);
			
		}
		}
		
		public void ifInValueMethod(Hashtable attributes, boolean inValue, boolean bTagName, StringBuffer sbActual, boolean bLeftAttribute, StringBuffer sbAttributeName, StringBuffer sbAttributeValue, char cChar) {
			if (!inValue){
				ifBTagNameMethod(attributes,bTagName, sbAttributeName, sbAttributeValue, bLeftAttribute);
				
				sbActual = sbAttributeName;
			}else{
				sbActual.append(cChar);
			}
		}
		
		public void ifBTagNameMethod(Hashtable attributes, boolean bTagName,StringBuffer sbAttributeName, StringBuffer sbAttributeValue,boolean bLeftAttribute) {
			if (bTagName){
				bTagName = false;
			}else{
				String sAttributeName = sbAttributeName.toString();
				String sAttributeValue = sbAttributeValue.toString();
				
				attributes.put(sAttributeName, sAttributeValue);

				sbAttributeName = new StringBuffer();
				sbAttributeValue = new StringBuffer();
				bLeftAttribute = false;
			}
		}
		
		public void ifCaseInValueMethod(char cChar, boolean inValue, StringBuffer sbActual, StringBuffer sbAttributeValue,boolean bLeftAttribute) {
			if (!inValue){
				sbActual = sbAttributeValue;
				bLeftAttribute = true;
			}else{
				sbActual.append(cChar);
			}
		}
		
		public void tryCaseMethod(boolean inValue, StringBuffer sbActual) {
			try{
				if (!inValue && sbActual.charAt(sbActual.length() - 1) == ' '){
					sbActual.deleteCharAt(sbActual.length() - 1);
				}
			}catch (java.lang.StringIndexOutOfBoundsException e){
				System.out.println(sbActual.toString());
			}
			
		}
		
		public void ifInValueMethod2(boolean inValue, StringBuffer sbActual, char cChar ) {
			if (inValue)
				sbActual.append(cChar);	
		}
		
		public void ifBLeftAttributeCaseMethod(StringBuffer sbTagName, boolean bLeftAttribute, Hashtable attributes, StringBuffer sbAttributeName,char oChar, StringBuffer sbAttributeValue)
		{
			
			if (bLeftAttribute){
				String sAttributeName = sbAttributeName.toString();
				String sAttributeValue = sbAttributeValue.toString();
				attributes.put(sAttributeName, sAttributeValue);
			}
			String sTagName = sbTagName.toString();
			
			ifFirstTagMethod(sTagName, attributes, oChar);
			
		}
		
		public void ifFirstTagMethod(String sTagName, Hashtable attributes, char oChar) {
			if (firstTag){
				firstTag = false;
				
				ifSTagNameMethod(sTagName);
			
			}else{
				
			ifRootNodeMethod(sTagName, attributes, oChar);
				
			}

		}
		
		public void ifSTagNameMethod(String sTagName) {
			int x = 0;
			try {
				if (!(sTagName.equals("doctype") || sTagName.equals("?xml"))) x = 1;
			}
			catch(RuntimeException e)
			{
				System.out.println("XML File has no valid header");
			}
					}
		
		public void ifRootNodeMethod(String sTagName, Hashtable attributes, char oChar) {
			if (rootNode && !firstTag){
				rootNode = false;
				result = new XMLElement(sTagName, attributes);
				actualElement = result;
			}else{
				XMLElement keep = new XMLElement(sTagName, attributes);
				actualElement.addChild(keep);
				
				ifOCharMethod(oChar);
				
			}
		}
		
		public void ifOCharMethod(char oChar)
		{
			if (oChar != '/')
				actualElement = keep;	
		}
		//____________________________________________________________________
		/**
		 * Parses a TemplateTag and extracts its Name and Attributes.
		 * @param page Reader
		 * @param alreadyParsed StringBuffer
		 * @return Reader
		 * @throws Exception
		 */
		private Reader handleStartTag(Reader page, StringBuffer alreadyParsed) throws Exception{
			int iChar;
			char cChar;

			boolean bTagName = true;
			boolean bSpaceBefore = false;
			boolean bLeftAttribute = false;

			StringBuffer sbTagName = alreadyParsed;
			StringBuffer sbAttributeName = new StringBuffer();
			StringBuffer sbAttributeValue = new StringBuffer();
			StringBuffer sbActual = sbTagName;

			Hashtable attributes = new Hashtable();
			boolean inValue = false;
			char oChar = ' ';
			int read2=page.read();//mio
			while ((iChar = read2) != -1){
				cChar = ' ';
				
				//switchcCharMethod();
				
				
				
				
				oChar = cChar;
			}

					}

		//_________________________________________________________________________
		
		public String switchCaseMethod(char cChar) {
			String toParse;
			try {
				
			
			switch (cChar){
			case  '\b':
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
			catch (Exception e) {
				System.out.println("Error in line:"+line);
			}
			
		}
		
		//_________________________________________________________________________
		
		/**
		 * Parses the end tags of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		private Reader handleEndTag(XMLElement xmlElement, Reader toParse) throws Exception{
			int iChar;
			char cChar;
			int read3 = toParse.read();//mio
			while ((iChar = read3) != -1){

				cChar = ' ';
				
				switchCaseMethod(cChar);
				
				
			}
			
		}

		/**
		 * Parses the comments of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		private Reader handleComment(Reader toParse) throws Exception{
			int iChar;
			char cChar;
			char prevChar = ' ';
			int read4 = toParse.read();//mio
			try {
			while ((iChar = read4) != -1){
				cChar = ' ';
				if (prevChar == '-' && cChar == '>'){
					return toParse;
				}
				prevChar = cChar;
			}
		} catch(Exception e) {
			System.out.println("Comment is not correctly closed");
		}
		}
		
		/**
		 * Parses the Doctype section of a XML document
		 * 
		 * @param toParse Reader
		 * @return Reader
		 * @throws Exception
		 */
		private Reader handleDoctypeSection(Reader toParse) throws Exception{
			int iChar;
			char cChar;
			char prevChar = ' ';
			
			boolean entities = false;
			int read5=toParse.read();//mio
			try {
			while ((iChar = read5) != -1){
				cChar = ' ';
				if(cChar == '[')entities = true;
				if (cChar == '>'){
					if(prevChar == ']' && entities || !entities)
					return toParse;
				}
				prevChar = cChar;
			}
			}catch (Exception e)
			{
			System.out.println("Comment is not correctly closed in Line:"+line);
		}
		}
		//_______________________________________________________________
		
		public void ifCCharReaderMethod(char cChar, final StringBuffer stringBuffer) {
			if (cChar == ';'){
				final String entity = result.toString().toLowerCase();
				if (entity.equals("lt;"))
					stringBuffer.append("<");
				else if (entity.equals("gt;"))
					stringBuffer.append(">");
				else if (entity.equals("amp;"))
					stringBuffer.append("&");
				else if (entity.equals("quot;"))
					stringBuffer.append("\"");
				else if (entity.equals("apos;"))
					stringBuffer.append("'");
				break;
			}
		}
		
		public void ifThrowMethod(int counter){
				if (counter > 4)
					throw new Exception("Illegal use of &. Use &amp; entity instead. Line:"+line);
			
		}
		//_______________________________________________________________
		
		
		/**
		 * Parses Entities of a document
		 * 
		 * @param toParse
		 * @param stringBuffer
		 * @return
		 * @throws Exception
		 */
		private Reader handleEntity(Reader toParse, final StringBuffer stringBuffer) throws Exception{
			int iChar;
			char cChar;
			final StringBuffer result = new StringBuffer();
			int counter = 0;
			int read6= toParse.read();//mio
			while ((iChar = read6) != -1){
				cChar = ' ';
				result.append(cChar);
				
				ifCCharReaderMethod(cChar, stringBuffer);
				
				counter++;
				
				ifThrowMethod(counter);
				
			}

			return toParse;
		}

		/**
		 * Parses a CData Section of a document
		 * @param toParse
		 * @return
		 * @throws Exception
		 */
		private Reader handleCDATASection(Reader toParse) throws Exception{
			int iChar;
			char cChar;
			StringBuffer result = new StringBuffer();
			int counter = 0;
			boolean checkedCDATA = false;
			int read7=toParse.read();//mio
			while ((iChar = read7) != -1){
				cChar = ' ';
				if (cChar == ']'){
					XMLElement keep = new XMLElement(result.toString());
					keep.cdata = true;
					keep.pcdata = true;
					actualElement.addChild(keep);
					break;
				}
				result.append(cChar);
				counter++;
				if (counter > 5 && !checkedCDATA){
					checkedCDATA = true;
					if (!result.toString().toUpperCase().equals("CDATA["))
						throw new RuntimeException(
							"Illegal use of <![. " + 
							"These operators are used to start a CDATA section. <![CDATA[]]>" +
							" Line:" + line
						);
					result = new StringBuffer();
				}
			}

			if ((char) toParse.read() != ']')
				throw new RuntimeException("Wrong Syntax at the end of a CDATA section <![CDATA[]]> Line:"+line);
			if ((char) toParse.read() != '>')
				throw new RuntimeException("Wrong Syntax at the end of a CDATA section <![CDATA[]]> Line:"+line);

			//XMLElement keep = new XMLElement(sTagName,attributes);
			//actualElement.addChild(keep);
			//if(oChar != '/')actualElement = keep;
			return toParse;
		}
		XMLElement xmlElement;
		public void run(){
			xmlElement = parseDocument(document);
			
			if(xmlHandler == null)  return;
			
			try{
				xmlEventMethod.invoke(xmlHandler, new Object[] {xmlElement});
			}catch (IllegalAccessException e){
				// TODO Auto-generated catch block
				System.out.println("Something was wrong");
			}catch (InvocationTargetException e){
				// TODO Auto-generated catch block
				System.out.println("Something was wrong");
			}catch(NullPointerException e){
				throw new RuntimeException("You need to implement the xmlEvent() function to handle the loaded xml files.");
			}
		}

	}

	/**
	 * XML document start
	 */
	private static final String docStart = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";

	/**
	 * the parent element to put the children elements in while parsing
	 */
	private XMLElement actualElement;

	/**
	 * the result element for loading a document
	 */
	private XMLElement result;

	/**
	 * Parent PApplet instance
	 */
	private final PApplet pApplet;
	private final Object parent;

	/**
	 * Method to call when xml is loaded
	 */
	private Method xmlEventMethod;

	/**
	 * Initializes a new XMLInOut Object for loading and saving XML files. If you give
	 * the constructor only a reference to your application it looks for the xmlEvent
	 * method in your sketch. If you also give it a reference to a further object it
	 * calls the xmlEvent method in this object.
	 * @param pApplet PApplet, the Applet proXML is running in
	 */
	public XMLInOut(final PApplet pApplet){
		this.pApplet = pApplet;
		parent = pApplet;

		try{
			xmlEventMethod = pApplet.getClass().getMethod("xmlEvent", new Class[] {XMLElement.class});
		}catch (Exception e){
			System.out.println("Descriptive error");//mio
		}
	}

	/**
	 * @param pApplet PApplet, the Applet proXML is running in
	 * @param i_parent Object, the object that contains the xmlEvent function
	 */
	public XMLInOut(final PApplet pApplet, final Object i_parent){
		this.pApplet = pApplet;
		parent = i_parent;

		try{
			xmlEventMethod = i_parent.getClass().getMethod("xmlEvent", new Class[] {XMLElement.class});
		}catch (Exception e){
			System.out.println("Descriptive error");//mio
		}
	}

	//__________________________________________________________________________
	
	public InputStream tryBlocMethod(InputStream stream, String filename) {
		try{
			URL url = new URL(filename);
			stream = url.openStream();
			return stream;

		}catch (MalformedURLException e){
			// not a url, that's fine
			System.out.println("Descriptive error");//mio
		}catch (IOException e){
			throw new RuntimeException("Error downloading from URL " + filename);
		}
	}
	
	public void ifPAppletMethod(InputStream stream, string filename) {

		if (!pApplet.online){
			
			tryBlocMethod2TryTry(stream, filename);
			
		}
	}
	
	public InputStream tryBlocMethod2TryTry(InputStream stream, String filename) {
		try{
			// first see if it's in a data folder
			File file = new File(pApplet.sketchPath + File.separator + "data", filename);
			
			ifFileMethod(file, filename);
			
			

			// if this file is ok, may as well just load it
			stream = new FileInputStream(file);
			if (stream != null)
				return stream;

			// have to break these out because a general Exception might
			// catch the RuntimeException being thrown above
		}catch (IOException ioe){
			System.out.println("Descriptive error");//mio
		}catch (SecurityException se){
			System.out.println("Descriptive error");//mio
		}
	}
	
	public void ifFileMethod(File file, String filename) 
	{
		
	if (!file.exists()){
	
		// next see if it's just in this folder
		file = new File(pApplet.sketchPath, filename);
	}
	
	ifFileexistsMethod(file, filename);
	
	}
	
	public void ifFileexistsMethod(File file, String filename) {
		if (file.exists()){
			
			ifTryBlocMethod(file, filename);
		
		}
	}
	
	public void ifTryBlocMethod(File file, String filename) {
		try{
			String path = file.getCanonicalPath();
			String filenameActual = new File(path).getName();
			// if the actual filename is the same, but capitalized
			// differently, warn the user. unfortunately this won't
			// work in subdirectories because getName() on a relative
			// path will return just the name, while 'filename' may
			// contain part of a relative path.
			
			ifFilnameActualMethod(filenameActual, filename);
			
			
		}catch (IOException e){
			System.out.println("Descriptive error");//mio
		}
	}
	
	public void ifFilnameActualMethod(String filenameActual, String filename) {
		if (filenameActual.equalsIgnoreCase(filename) && !filenameActual.equals(filename)){
			throw new RuntimeException("This file is named " + filenameActual + " not " + filename + ".");
		}
	}
	
	public void tryBlocMethod2(InputStream stream, String filename) {
		try{
			// by default, data files are exported to the root path of the jar.
			// (not the data folder) so check there first.
			stream = pApplet.getClass().getResourceAsStream(filename);
			
			ifStreamMethod(stream);
			
			// hm, check the data subfolder
			stream = pApplet.getClass().getResourceAsStream("data/" + filename);
			
			ifStreamMethod2(stream);
			
			// attempt to load from a local file, used when running as
			// an application, or as a signed applet
			
			tryTryMethod(stream, filename);
			
			tryMethodtry();
			
			
			ifStreamMethodtry(stream, filename);
			
			
		}catch (Exception e){
			System.out.println("Descriptive error");//mio
		}
	}
	
	public InputStream ifStreamMethod(InputStream stream)
	{
		if (stream != null)
			return stream;	
	}
	
	public InputStream ifStreamMethod2(InputStream stream) {
		if (stream != null)
			return stream;
	}
	
	public InputStream tryTryMethod(InputStream stream, String filename) {
		try{
			File file = new File(pApplet.sketchPath, filename);
			stream = new FileInputStream(file);
			if (stream != null)
				return stream;

		}catch (Exception e){
			System.out.println("Descriptive error");//mio
		} // ignored

		tryTryMethod2(stream, filename);
		
		tryTryMethod3(stream, filename);
		
	}
	
	public InputStream tryTryMethod2(InputStream stream, String filename) {
		try{
			stream = new FileInputStream(new File("data", filename));
			if (stream != null)
				return stream;
		}catch (IOException e2){
			System.out.println("Descriptive error");//mio
		}	
	}
	
	public InputStream tryTryMethod3(InputStream stream, String filename) {
		try{
			stream = new FileInputStream(filename);
			if (stream != null)
				return stream;
		}catch (IOException e1){
			System.out.println("Descriptive error");//mio
		}
	}
	
	public void tryMethodtry() {
		try{ // first try to catch any security exceptions
			

		}catch (SecurityException se){
			System.out.println("Descriptive error");//mio
		} // online, whups

	}
	
	public void ifStreamMethodtry(InputStream stream, String filename) {
		if (stream == null){
			throw new IOException("openStream() could not open " + filename);
		}
	}
	
	
		//__________________________________________________________________________
	
	/**
	 * Modified openStream Method from PApplet.
	 * @param filename
	 * @return InputStream
	 */
	private InputStream openStream(String filename){
		InputStream stream = null;

		tryBlocMethod(stream, filename);
		
		// if not online, check to see if the user is asking for a file
		// whose name isn't properly capitalized. this helps prevent issues
		// when a sketch is exported to the web, where case sensitivity
		// matters, as opposed to windows and the mac os default where
		// case sensitivity does not.
		
		ifPAppletMethod(stream);
		
		tryBlocMethod2(stream);

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
	//cngcg
	public void loadElement(final String documentUrl){

		Thread loader;
		if (documentUrl.startsWith("<?xml")){
			loader = new Thread(new Loader(new StringReader(documentUrl),parent));
		}else{
			try{
				InputStream test = openStream(documentUrl);
				loader = new Thread(new Loader(new BufferedReader(new InputStreamReader(test)),parent));
			}catch (Exception e){
				throw new RuntimeException("proXML was not able to load the given xml-file: " + documentUrl + " Please check if you have entered the correct url.");
			}
		}
		try{
			loader.start();
		}catch (Exception e){
			throw new RuntimeException("proXML was not able to read the given xml-file: " + documentUrl + " Please make sure that you load a file that contains valid xml.");
		}
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
	 * @param documentUrl String, url from where the Element has to be loaded
	 * @example proxml_loadElementFrom
	 * @shortdesc Loads the XMLElement from a given path.
	 * @related XMLInOut
	 * @related loadElementFrom ( )
	 * @related saveElement ( )
	 */
	public XMLElement loadElementFrom(final String documentUrl){
		Loader loader;
		if (documentUrl.startsWith("<?xml")){
			loader = new Loader(new StringReader(documentUrl),null);
		}else{
			try{
				InputStream test = openStream(documentUrl);
				loader = new Loader(new BufferedReader(new InputStreamReader(test)),null);
			}catch (Exception e){
				throw new RuntimeException("proXML was not able to load the given xml-file: " + documentUrl + " Please check if you have entered the correct url.");
			}
		}
		try{
			loader.run();
			return loader.xmlElement;
		}catch (Exception e){
			throw new RuntimeException("proXML was not able to read the given xml-file: " + documentUrl + " Please make sure that you load a file that contains valid xml.");
		}
	}

	/**
	 * Saves the XMLElement to a given Filename.
	 * 
	 * @param documentUrl String, url to save the XMLElement as XML File 
	 * @example proxml
	 * @shortdesc Saves the XMLElement to a given File.
	 * @related XMLInOut
	 * @related loadElement ( )
	 * @usage Application
	 */
	public void saveElement(final XMLElement xmlElement, String filename){
		File file = new FileOutputStream();
		PrintWriter output = new PrintWriter(file);
		try{
			
			if (!pApplet.online){
				file = new File(pApplet.sketchPath + File.separator + "data", filename);
				System.out.println(pApplet.sketchPath + File.separator + "data");
				if (!file.exists()){
					final String parent = file.getParent();

					if (parent != null){
						File unit = new File(parent);
						if (!unit.exists())
							unit.mkdirs();
					}
				}
			}else{
				file = new File(pApplet.getClass().getResource("data/" + filename).toURI());
			}

			output.println(docStart);
			xmlElement.printElementTree(output, "  ");
			output.flush();
			output.close();
		}catch (Exception e){
			System.out.println("Something was wrong");
			System.out.println(pApplet.sketchPath + File.separator + "data");
			System.out.println("You cannot write to this destination. Make sure destionation is a valid path");
		}
		finally {
			if(output != null) {
				try {
					output.close();
				} catch(Exception e) {
					System.out.println(pApplet.sketchPath + File.separator + "data");
					System.out.println("You cannot write to this destination. Make sure destionation is a valid path");

				}
			}
		}
	}

	/**
	 * The following methods are for parsing the XML Files
	 */

}
