package org.json;

//hjdjioxcj

/*
Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

import java.util.Iterator;


/**
 * This provides static methods to convert an XML text into a JSONObject,
 * and to covert a JSONObject into an XML text.
 * @author JSON.org
 * @version 2010-04-08
 */
public class XML {

    /** The Character '&'. */
    public static final Character AMP   = new Character('&');

    /** The Character '''. */
    public static final Character APOS  = new Character('\'');

    /** The Character '!'. */
    public static final Character BANG  = new Character('!');

    /** The Character '='. */
    public static final Character EQ    = new Character('=');

    /** The Character '>'. */
    public static final Character GT    = new Character('>');

    /** The Character '<'. */
    public static final Character LT    = new Character('<');

    /** The Character '?'. */
    public static final Character QUEST = new Character('?');

    /** The Character '"'. */
    public static final Character QUOT  = new Character('"');

    /** The Character '/'. */
    public static final Character SLASH = new Character('/');

    /**
     * Replace special characters with XML escapes:
     * <pre>
     * &amp; <small>(ampersand)</small> is replaced by &amp;amp;
     * &lt; <small>(less than)</small> is replaced by &amp;lt;
     * &gt; <small>(greater than)</small> is replaced by &amp;gt;
     * &quot; <small>(double quote)</small> is replaced by &amp;quot;
     * </pre>
     * @param string The string to be escaped.
     * @return The escaped string.
     */
    
    static class xml1 {
    	

    public static String escape(String string) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = string.length(); i < len; i++) {
            char c = string.charAt(i);
            switch (c) {
            case '&':
                sb.append("&amp;");
                break;
            case '<':
                sb.append("&lt;");
                break;
            case '>':
                sb.append("&gt;");
                break;
            case '"':
                sb.append("&quot;");
                break;
            default:
                sb.append(c);
                break;
            }
        }
        return sb.toString();
    }
    
    /**
     * Throw an exception if the string contains whitespace. 
     * Whitespace is not allowed in tagNames and attributes.
     * @param string
     * @throws JSONException
     */
    public static void noSpace(String string) throws JSONException {
    	int i, length = string.length();
    	if (length == 0) {
    		throw new JSONException("Empty string.");
    	}
    	for (i = 0; i < length; i += 1) {
		    if (Character.isWhitespace(string.charAt(i))) {
		    	throw new JSONException("'" + string + 
		    			"' contains a space character.");
		    }
		}
    }

    //_________________________________________________________________
    
    public static boolean ifNedestMethod(Object t, char c, XMLTokener x, String s, int i, String name, String n, JSONObject o) {
    	

    	if (t == BANG) {
            c = x.next();
            
            
            if (c == '-') {
            	
            	if1Method(x);
            	
                x.back();
            } else if (c == '[') {
                t = x.nextToken();
                
                if2Method(x, t, s);
                                
                throw x.syntaxError("Expected 'CDATA['");
            }
            i = 1;
            
            doMethod(t, x, i);
             
            return false;
        } else if (t == QUEST) {

// <?
            x.skipPast("?>");
            return false;
        } else if (t == SLASH) {

// Close tag </

        	t = x.nextToken();
        	
        	if6Method(t, x, name);
        	

        } else if (t instanceof Character) {
            throw x.syntaxError("Misshaped tag");

// Open tag <

        } else {
            n = (String)t;
            t = null;
            o = new JSONObject();
            
            forMethod(x, t, s, o );

            
        }
        
    }
    
    
    public static boolean if1Method(XMLTokener x) {
    	if (x.next() == '-') {
            x.skipPast("-->");
            return false;
        }	
    }
    
    public static boolean if2Method(XMLTokener x, Object t,  String s) {
    	 if (t.equals("CDATA")) {
    		 
    		 if4Method(x, s);
    		 
         }
    }
    
    public static boolean doMethod(Object t, XMLTokener x, int i) {
    	do {
            t = x.nextMeta();
            
            if3Method(t, x, i);
            
        } while (i > 0);
    }
    
    public static boolean if3Method(Object t, XMLTokener x, int i) {
    	if (t == null) {
            throw x.syntaxError("Missing '>' after '<!'.");
        } else if (t == LT) {
            i += 1;
        } else if (t == GT) {
            i -= 1;
        }
    }
    
    public static boolean if4Method(XMLTokener x, String s, JSONObject context) {
    	if (x.next() == '[') {
            s = x.nextCDATA();
            
            if5Method(s, context);
            
            return false;
        }
    }
    
    public static boolean if5Method(String s, JSONObject context) {
    	if (s.length() > 0) {
            context.accumulate("content", s);
        }
    }
    
    public static boolean if6Method(Object t, XMLTokener x, String name) {
    	   if (name == null) {
               throw x.syntaxError("Mismatched close tag" + t);
           }            
    	   
    	   if7Method(t, name, x);
    	   
           return true;
           
    }
    
    public static boolean if7Method(Object t, String name, XMLTokener x) {
    	if (!t.equals(name)) {
            throw x.syntaxError("Mismatched " + name + " and " + t);
        }
    	
    	if8Method(x);
    	
    }
    
    public static boolean if8Method(XMLTokener x) {
    	 if (x.nextToken() != GT) {
             throw x.syntaxError("Misshaped close tag");
         }
    }
    
    public static boolean forMethod(XMLTokener x, Object t, String s, JSONObject o ) {
    	for (;;) {
    		
    		if9Method(t,x);
    		
//attribute = value

    		if10Method(t, s, x, o);
    		
            
        }
    }
    }
    
    public static boolean if9Method(Object t, XMLTokener x) {
    	if (t == null) {
            t = x.nextToken();
        }
    }
    
    public static boolean of10Method(Object t, String s, XMLTokener x, JSONObject o, String n, JSONObject context ) {
    	if (t instanceof String) {
            s = (String)t;
            t = x.nextToken();
            
            if11Method(t, x, o);
            
//Empty tag <.../>

        } else if (t == SLASH) {
        	
        	if13Method( o, context, n, x);

//Content, between <...> and </...>

        } else if (t == GT) {
        	
        	for2Method(n, t, s, o, context, x);
        	
           
        } else {
            throw x.syntaxError("Misshaped tag");
        }
    }
    
    public static boolean if11Method(Object t, XMLTokener x, JSONObject o, String s) {
    	if (t == EQ) {
            t = x.nextToken();
            
            if12Method(t, x);
            
            o.accumulate(s, JSONObject.stringToValue((String)t));
            t = null;
        } else {
            o.accumulate(s, "");
        }
    }
    
    public static boolean if12Method(Object t, XMLTokener x) {
    	if (!(t instanceof String)) {
            throw x.syntaxError("Missing value");
        }
    }
    
    public static boolean if13Method(JSONObject o, JSONObject context, String n, XMLTokener x) {
    	 if (x.nextToken() != GT) {
             throw x.syntaxError("Misshaped tag");
         }
    	 
    	 if14Method(o, context, n);
    	 
         
         return false;
    }
    
    public static boolean if14Method(JSONObject o, JSONObject context, String n) {
    	if (o.length() > 0) {
            context.accumulate(n, o);
        } else {
        	context.accumulate(n, "");
        }
    }
    
    public static boolean for2Method(String n, Object t, String s, JSONObject o,  JSONObject context, XMLTokener x) {
    	 for (;;) {
             t = x.nextContent();
             
             if15Method(n, t, s, o, context, x);
    	 }   
    }
    	 
    public static boolean if15Method(String n, Object t, String s, JSONObject o, JSONObject context, XMLTokener x) {
    	 if (t == null) {
    		 
    		 if16Method(n, x);
             
             return false;
         } else if (t instanceof String) {
             s = (String)t;
             
             if17Method(s,o);
             
//Nested element

         } else if (t == LT) {
        	 
        	 if18Method( o, context, n, x);
        	 
         }
     }
   
    
    public static boolean if16Method(String n, XMLTokener x) {
    	 if (n != null) {
             throw x.syntaxError("Unclosed tag " + n);
         }
    }
    
    public static boolean if17Method(String s, JSONObject o) {

        if (s.length() > 0) {
            o.accumulate("content", JSONObject.stringToValue(s));
        }
    }
    
    public static boolean if18Method(JSONObject o, JSONObject context, String n, XMLTokener x) {
    	if (parse(x, o, n)) {
    		
    		if19Method(o, context, n);
            
            return false;
        }
    }
    
    public static boolean if19Method(JSONObject o, JSONObject context, String n) {
    	 if (o.length() == 0) {
             context.accumulate(n, "");
         } else if (o.length() == 1 &&
                o.opt("content") != null) {
             context.accumulate(n, o.opt("content"));
         } else {
             context.accumulate(n, o);
         }
    }
    //_________________________________________________________________
    /**
     * Scan the content following the named tag, attaching it to the context.
     * @param x       The XMLTokener containing the source string.
     * @param context The JSONObject that will include the new material.
     * @param name    The tag name.
     * @return true if the close tag is processed.
     * @throws JSONException
     */
    private static boolean parse(XMLTokener x, JSONObject context,
                                 String name) throws JSONException {
        char       c;
        int        i;
        String     n;
        JSONObject o = null;
        String     s;
        Object     t;

// Test for and skip past these forms:
//      <!-- ... -->
//      <!   ...   >
//      <![  ... ]]>
//      <?   ...  ?>
// Report errors for these forms:
//      <>
//      <=
//      <<

        t = x.nextToken();

// <!

        ifNedestMethod(t, c, x, s, i, name, n, o);
        
        
        
        
    }


    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONObject. Some information may be lost in this transformation
     * because JSON is a data format and XML is a document format. XML uses
     * elements, attributes, and content text, while JSON uses unordered
     * collections of name/value pairs and arrays of values. JSON does not
     * does not like to distinguish between elements and attributes.
     * Sequences of similar elements are represented as JSONArrays. Content
     * text may be placed in a "content" member. Comments, prologs, DTDs, and
     * <code>&lt;[ [ ]]></code> are ignored.
     * @param string The source string.
     * @return A JSONObject containing the structured data from the XML string.
     * @throws JSONException
     */
    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject o = new JSONObject();
        XMLTokener x = new XMLTokener(string);
        boolean a=x.more();//mio
        boolean b=x.skipPast("<");//mio
        while (a && b) {
            parse(x, o, null);
        }
        return o;
    }


    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     * @param o A JSONObject.
     * @return  A string.
     * @throws  JSONException
     */
    public static String toString(Object o) throws JSONException {
        return toString(o, null);
    }

    //_______________________________________________________________________
    
public static String ifStringMethod(Object o, String tagName, StringBuffer b, Object v, JSONObject jo, Iterator keys, String k, String s, int len, JSONArray ja, int i) {
	
	   if (o instanceof JSONObject) {

		// Emit <tagName>
		   
		   ifTagNameMethod(tagName, b);

		// Loop thru the keys.

		            jo = (JSONObject)o;
		            keys = jo.keys();
		            
		            whileKeysMethod(v, s, keys, jo, k, len, b, ja, i );
		            
		            tagNameIfMethod(tagName, b);
		            
		            return b.toString();

		// XML does not have good support for arrays. If an array appears in a place
		// where XML is lacking, synthesize an <array> element.

		        } else if (o instanceof JSONArray) {
		            ja = (JSONArray)o;
		            len = ja.length();
		            
		            forTagNameMethod(i, tagName, b, v, ja, len);
		            
		            return b.toString();
		        } else {
		        	
		        	elseMethod(s,o, tagName);
		        	
		        }
		    }

    
    public static String elseMethod(String s, Object o, String tagName) {

        s = (o == null) ? "null" : escape(o.toString());
        return (tagName == null) ? "\"" + s + "\"" :
            (s.length() == 0) ? "<" + tagName + "/>" :
            "<" + tagName + ">" + s + "</" + tagName + ">";
    }
    
    public static String forTagNameMethod(int i, String tagName, StringBuffer b, Object v, JSONArray ja, int len)
    {
    	for (i = 0; i < len; ++i) {
        	v = ja.opt(i);
            b.append(toString(v, (tagName == null) ? "array" : tagName));
        }	
    }
    public static String tagNameIfMethod(String tagName, StringBuffer b ) {
    	 if (tagName != null) {

    			// Emit the </tagname> close tag

    			                b.append("</");
    			                b.append(tagName);
    			                b.append('>');
        }
    }
    
   public static String ifTagNameMethod(String tagName, StringBuffer b ) {
	   if (tagName != null) {
           b.append('<');
           b.append(tagName);
           b.append('>');
       }   
   }
   
   public static String whileKeysMethod(Object v, String s, Iterator keys, JSONObject jo, String k, int len,StringBuffer b, JSONArray ja, int i ) {
	   while (keys.hasNext()) {
           k = keys.next().toString();
           v = jo.opt(k);
           
           ifKeyMethod(v);
           
           ifKey2Method(v, s);
           
// Emit content in body

           ifkMethod(i, b, ja, v, k, len);
	   }   
   }
   
   public static String ifKeyMethod(Object v) {
	   if (v == null) {
          	v = "";
          }
   }
   
   public static String ifKey2Method(Object v, String s) {
	   if (v instanceof String) {
           s = (String)v;
       } else {
           s = null;
       }
   }
   
   public static String ifKMethod(int i, StringBuffer b, JSONArray ja, Object v, String k, int len) {
	   if (k.equals("content")) {
		   
		   ifVMethod(i, b, ja, v, len);
		   

//Emit an array of similar keys

       } else if (v instanceof JSONArray) {
           ja = (JSONArray)v;
           len = ja.length();
           
         forI2Method(i, v, b, k, len, ja);
           
       } else if (v.equals("")) {
           b.append('<');
           b.append(k);
           b.append("/>");

//Emit a new tag <k>

       } else {
           b.append(toString(v, k));
       }
   }
   
   
   public static String ifVMethod(int i, StringBuffer b, JSONArray ja, Object v, int len) {
	   if (v instanceof JSONArray) {
           ja = (JSONArray)v;
           len = ja.length();
           
           forIMethod(i, b, ja, len);
           
          
       } else {
           b.append(escape(v.toString()));
       }   
   }
   
   public static String forIMethod(int i, StringBuffer b, JSONArray ja, int len) {
	   for (i = 0; i < len; i += 1) {
		   
		   ifForMethod(i, b);
           
           b.append(escape(ja.get(i).toString()));
       }
   }
   
   public static String ifForMethod(int i, StringBuffer b) {
	   if (i > 0) {
           b.append('\n');
       }
   }
   
   public static String forI2Method(int i, Object v, StringBuffer b, String k, int len, JSONArray ja) {
	   for (i = 0; i < len; i += 1) {
          	v = ja.get(i);
          	
          	ifVForMethod(v, b, k);    
   }
  }
   
   public static String ifVForMethod(Object v, StringBuffer b, String k) {
	   if (v instanceof JSONArray) {
           b.append('<');
           b.append(k);
           b.append('>');
   		b.append(toString(v));
           b.append("</");
           b.append(k);
           b.append('>');
   	} else {
   		b.append(toString(v, k));
   	}
   }
    //_________________________________________________________________________
    /**
     * Convert a JSONObject into a well-formed, element-normal XML string.
     * @param o A JSONObject.
     * @param tagName The optional name of the enclosing tag.
     * @return A string.
     * @throws JSONException
     */
    public static String toString(Object o, String tagName)
            throws JSONException {
        String b;//mio
        int          i;
        JSONArray    ja;
        JSONObject   jo;
        String       k;
        Iterator     keys;
        int          len;
        String       s;
        Object       v;
        
        ifStringMethod( o, tagName, b, v, jo, keys, k, s, len, ja);
    }
}
