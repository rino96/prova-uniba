package org.json;
//annarita pphh

/*
Copyright (c) 2008 JSON.org

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
 * This provides static methods to convert an XML text into a JSONArray or 
 * JSONObject, and to covert a JSONArray or JSONObject into an XML text using 
 * the JsonML transform.
 * @author JSON.org
 * @version 2010-02-12
 */
public class JSONML {
		
    /**
     * Parse XML values and store them in a JSONArray.
     * @param x       The XMLTokener containing the source string.
     * @param arrayForm true if array form, false if object form.
     * @param ja      The JSONArray that is containing the current tag or null
     *     if we are at the outermost level.
     * @return A JSONArray if the value is the outermost tag, otherwise null.
     * @throws JSONException
     * 
     */
	
	//______________________________________________________________________
	
	public static String controlloToken(Object token, XMLTokener x) {
		
		if (!(token instanceof String)) {
    		throw new JSONException(
    				"Expected a closing name instead of '" + 
    				token + "'.");
    	}
        if (x.nextToken() != XML.GT) {
            throw x.syntaxError("Misshaped close tag");
        }
        return (String)token;
		
	}
	//______________________________________________________________
	
public void ifCMethod(XMLTokener x) {
	 if (x.next() == '-') {
         x.skipPast("-->");
     }
}
	
	public static void controlloC(char c,XMLTokener x, Object token ) {
	 
    if (c == '-') {
        if (x.next() == '-') {
            x.skipPast("-->");
        }
        x.back();
    } else if (c == '[') {
        token = x.nextToken();
        if (c == '-') {
        	
        	ifCMethod(x);
           
            x.back();
        } else if (c == '[') {
            token = x.nextToken();
}
        
        
    }
    }        
        
        
	//_____________________________________________________________        
        
        public static void ControlloJa(XMLTokener x,boolean arrayForm, JSONArray ja ) {
        	if (ja != null) {
        		ja.put(x.nextCDATA());
        	}
         else {
        	throw x.syntaxError("Expected 'CDATA['");
        }
        	
        }
        //____________________________________________________________
        
        public static String doParseWhile(Object token,int i, XMLTokener x ){
        	
        	i = 1;
            do {
                token = x.nextMeta();
                if (token == null) {
                    throw x.syntaxError("Missing '>' after '<!'.");
                } else if (token == XML.LT) {
                    i += 1;
                } else if (token == XML.GT) {
                    i -= 1;
                }
            } while (i > 0);
        	return (String)token;
        }
        
        
        //____________________________________________________________
        
        
        public static void openTagMethod(XMLTokener x, Object token, JSONArray  newja, String tagName, JSONObject newjo, boolean arrayForm,JSONArray ja)
        {
        	if (!(token instanceof String)) {
	            throw x.syntaxError("Bad tagName '" + token + "'.");		        		
        	}
        	tagName = (String)token;
            newja = new JSONArray();		
            newjo = new JSONObject();
        	if (arrayForm) {
	            newja.put(tagName);
	            if (ja != null) {
	            	ja.put(newja);
	            }
	        } else {
        		newjo.put("tagName", tagName);
        		if (ja != null) {
	            	ja.put(newjo);
	            }
	        }
        	
        }
        
        
        
        //____________________________________________________________
        
        
        public static void openTagControlMethod(XMLTokener x,Object token) {
        	
        	token = null;
            for (;;) {
                if (token == null) {
                    token = x.nextToken();
                }
                if (token == null) {
                	throw x.syntaxError("Misshaped tag");
                }
                if (!(token instanceof String)) {
                	break;
                }

        
            }
                
        }
        	//_____________________________________________________________
            
            public static void arrayFormMethod(XMLTokener x,Object token, String attribute,JSONObject newjo, boolean arrayForm) 
            {
            	
            	attribute = (String)token;
            	String s= "tagName";//mio
            	String s1="childNode";//mio
	        	if (!arrayForm && (attribute.equals(s) || attribute.equals(s1))) {//modificato me
                    throw x.syntaxError("Reserved attribute.");			        		
	        	}
                token = x.nextToken();
                if (token == XML.EQ) {
                    token = x.nextToken();
                    if (!(token instanceof String)) {
                        throw x.syntaxError("Missing value");
                    }
                    newjo.accumulate(attribute, JSONObject.stringToValue((String)token));
                    token = null;
                } else {
                	newjo.accumulate(attribute, "");
                }
            	
            }
            	//____________________________________________________________
            
            public static void emptyTagMethod(XMLTokener x,Object token, JSONArray  newja,JSONObject newjo, boolean arrayForm, JSONArray ja) 
            {
            	
                if (token == XML.SLASH) {
                    if (x.nextToken() != XML.GT) {
                        throw x.syntaxError("Misshaped tag");
                    }
                    if (ja == null) {
                    	if (arrayForm) {
                    		return newja;
                    	} else {
                    		return newjo;
                    	}
                    }

                }
            	
            	
            	
            }
            //_________________________________________________________________
            
            public static void tagChiusuraMethod(XMLTokener x, String closeTag,String tagName)
            {
     
            	if (!closeTag.equals(tagName)) {
            		throw x.syntaxError("Mismatched '" + tagName + 
            				"' and '" + closeTag + "'");
		        }
            
            }
            //___________________________________________________
            
            public static String jaNullMethod(boolean arrayForm, JSONArray ja, JSONArray  newja, JSONObject newjo)
            {
            	
            	if (ja == null) {
                	if (arrayForm) {
                		return newja;
                	} else {
                		return newjo;
                	}
            }
            
            	
            	}
            
            //___________________________________________________
            
            public static int vero1 (XMLTokener x,Object token,char c) 
            {
            	
            	if ((token instanceof Character) && (token == XML.SLASH)) 
            	{

            		// Close tag </
            		 	token = controlloToken(token, x);
  			        	token = x.nextToken();
  			        	return 0;
            	}else if (token == XML.BANG) {   		
  			        	// <!
  			          c = x.next();
  			          controlloC(c,x);
  			          return 1;
  			       }   		
              }


            //__________________________________________________
            public static String controlloCdata(XMLTokener x,Object token, String attribute, char c,
            	    String closeTag,int i, JSONArray  newja, JSONObject newjo, String tagName, 
            	    boolean arrayForm, JSONArray ja) {
            
            	if (token.equals("CDATA") && x.next() == '[') {
                	controlloJa(ja);
         
            } else {
            	 
            token = doParseWhile(x,token,attribute,c,closeTag,i,newja,newjo,tagName,arrayForm,ja);
	            return (String)token;
            }
            
            }
            
            //__________________________________________________
            
            public static int semplificaIfMethod(XMLTokener x,Object token,String attribute,int aux,char c,
            		String closeTag,int i, JSONArray  newja, JSONObject newjo,
            		String tagName, boolean arrayForm, JSONArray ja) {
            	
            	token = x.nextToken();
    			int vero = 1;
    			
    			aux = vero1( x,token, c);
    						 if (aux == 1) {
    						 
    						token = controlloCdata(x,token,attribute,c,closeTag,i,newja,newjo,tagName,arrayForm,ja);
    							 
    						 }
			                
			         else {
			      if (token == XML.QUEST) {
			    	  x.skipPast("?>");
			    	  return 1;
			        } else {
			            throw x.syntaxError("Misshaped tag");
			            openTagMethod( token, newja, tagName, newjo);
		        		openTagControlMethod( x,token);
		        		return 0;
			        }

		        				        		
		            }
            }//
            
            //_________________________________________________________________
            
            public static void arrayFormMethod1(XMLTokener x,Object token, String attribute, char c,
            	    String closeTag,int i, JSONArray  newja, JSONObject newjo, String tagName, 
            	    boolean arrayForm, JSONArray ja) {
            	
            	if (arrayForm && newjo.length() > 0) {
                	newja.put(newjo);
                	emptyTagMethod( x, token, newjo, arrayForm, ja);
                }
		
//Empty tag <.../>
                	
             // Content, between <...> and </...>	                
                else {
                	if (token != XML.GT) {
                		throw x.syntaxError("Misshaped tag");
                	}
        
                }
            }
            
            //_________________________________________________________________
            
            public static void arrayFormDiversa(XMLTokener x,Object token, String attribute, char c,
            	    String closeTag,int i, JSONArray  newja, JSONObject newjo, String tagName, 
            	    boolean arrayForm, JSONArray ja) {
            	if (!arrayForm && newja.length() > 0) {
        			newjo.put("childNodes", newja);
        		}
            }
            //_________________________________________________________________
        
            public static void ultimoIf(JSONArray ja, Object token) {
            	if (ja != null) {
		    		ja.put(token instanceof String ? 
		    				JSONObject.stringToValue((String)token) : token);
		    	}
            }
            
            
            //________________________________________________________________
	public static void parseWhile(XMLTokener x,Object token, String attribute, char c,
    String closeTag,int i, JSONArray  newja, JSONObject newjo, String tagName, 
    boolean arrayForm, JSONArray ja ) {
		
		int aux = 0;
		int eseguito = 0;
        while (true) {
        	token = x.nextContent();
        	
    		if (token == XML.LT) {
    	eseguito =semplificaIfMethod(x,token,attribute, aux, c, closeTag, i, newja, newjo,tagName, arrayForm, ja);
    			  
    				if (eseguito == 1)
    				{
    					arrayFormMethod1(x,token,attribute, c, closeTag, i, newja, newjo,tagName, arrayForm, ja);
    				}
                        	closeTag = (String)parse(x, arrayForm, newja);
	                	
	                	if (closeTag != null) {
	                	tagChiusuraMethod( x, closeTag,tagName);
		                // continuare da qua e fare i metodi	
		                	tagName = null;
		                	arrayFormDiversa(x,token,attribute, c, closeTag, i, newja, newjo,tagName, arrayForm, ja);
		            		
		            		 jaNullMethod( arrayForm, ja, newja, newjo);
		                			                	
	                	}                	
	                	
	            
    			else 
    				ultimoIf(ja,token);
        
	}
        }
	}
	//_______________________________
	
    private static String parse(XMLTokener x, boolean arrayForm, 
    		JSONArray ja) throws JSONException {
        String     attribute;
        char       c;
        String	   closeTag = null;
        int        i;
        JSONArray  newja = null;
        JSONObject newjo = null;
        Object     token;
        String	   tagName = null;
        
// Test for and skip past these forms:
//      <!-- ... -->
//      <![  ... ]]>
//      <!   ...   >
//      <?   ...  ?>
        parseWhile( x, token, attribute, c, closeTag, i, newja, newjo, tagName, arrayForm,ja );

    }


    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONArray using the JsonML transform. Each XML tag is represented as
     * a JSONArray in which the first element is the tag name. If the tag has
     * attributes, then the second element will be JSONObject containing the
     * name/value pairs. If the tag contains children, then strings and
     * JSONArrays will represent the child tags.
     * Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     * @param string The source string.
     * @return A JSONArray containing the structured data from the XML string.
     * @throws JSONException
     */
    public static JSONArray toJSONArray(String string) throws JSONException {
    	return toJSONArray(new XMLTokener(string));
    }


    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONArray using the JsonML transform. Each XML tag is represented as
     * a JSONArray in which the first element is the tag name. If the tag has
     * attributes, then the second element will be JSONObject containing the
     * name/value pairs. If the tag contains children, then strings and
     * JSONArrays will represent the child content and tags.
     * Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     * @param x An XMLTokener.
     * @return A JSONArray containing the structured data from the XML string.
     * @throws JSONException
     */
    public static JSONArray toJSONArray(XMLTokener x) throws JSONException {
    	return (JSONArray)parse(x, true, null);
    }


    
    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONObject using the JsonML transform. Each XML tag is represented as
     * a JSONObject with a "tagName" property. If the tag has attributes, then 
     * the attributes will be in the JSONObject as properties. If the tag 
     * contains children, the object will have a "childNodes" property which 
     * will be an array of strings and JsonML JSONObjects.

     * Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     * @param x An XMLTokener of the XML source text.
     * @return A JSONObject containing the structured data from the XML string.
     * @throws JSONException
     */
    public static JSONObject toJSONObject(XMLTokener x) throws JSONException {
       	return (JSONObject)parse(x, false, null);
    }
    /**
     * Convert a well-formed (but not necessarily valid) XML string into a
     * JSONObject using the JsonML transform. Each XML tag is represented as
     * a JSONObject with a "tagName" property. If the tag has attributes, then 
     * the attributes will be in the JSONObject as properties. If the tag 
     * contains children, the object will have a "childNodes" property which 
     * will be an array of strings and JsonML JSONObjects.

     * Comments, prologs, DTDs, and <code>&lt;[ [ ]]></code> are ignored.
     * @param string The XML source text.
     * @return A JSONObject containing the structured data from the XML string.
     * @throws JSONException
     */
    public static JSONObject toJSONObject(String string) throws JSONException {
    	return toJSONObject(new XMLTokener(string));
    }


    /**
     * Reverse the JSONML transformation, making an XML text from a JSONArray.
     * @param ja A JSONArray.
     * @return An XML string.
     * @throws JSONException
     */
    //_______________________________________________________
    
    public static StringBuffer emitAttributesMethod(StringBuffer sb,String v, String k,
    		JSONObject jo, Iterator keys) {
    	
    	while (keys.hasNext()) {
            k = keys.next().toString();
        	XML.noSpace(k);
            v = jo.optString(k);
            if (v != null) {
	            sb.append(' ');
	            sb.append(XML.escape(k));
	            sb.append('=');
	            sb.append('"');
	            sb.append(XML.escape(v));
	            sb.append('"');
            }
        }
    	return sb;
    }
    //______________________________________________________
    
    public static StringBuffer emitContentBodyMethod(StringBuffer sb,JSONArray ja, int i,
    		int length,Object e) {
    	
    	do {
		    e = ja.get(i);
		    i += 1;
		    if (e != null) {
		    	if (e instanceof String) {
		    		sb.append(XML.escape(e.toString()));
				} else if (e instanceof JSONObject) {
					sb.append(toString((JSONObject)e));
				} 
		    }
		} while (i < length);
    	return sb;
    }
    
    //______________________________________________________
    
    public static String toString(JSONArray ja) throws JSONException {
    	Object		 e;
    	int			 i;
    	JSONObject   jo;
    	String       k;
	    Iterator     keys;
	    int			 length;
    	StringBuffer sb = new StringBuffer();
	    String       tagName;
	    String       v;
	    
// Emit <tagName	    
    	
    	tagName = ja.getString(0);
		XML.noSpace(tagName);
		tagName = XML.escape(tagName);
		sb.append('<');
		sb.append(tagName);
		
		e = ja.opt(1);
		if (e instanceof JSONObject) {
			i = 2;
			jo = (JSONObject)e;
			
// Emit the attributes
			
	        keys = jo.keys();
	        sb = emitAttributesMethod( sb, v, k,jo, keys);
	          
		} else 
			i = 1;
		
	     	
//Emit content in body
	    	
		length = ja.length();
		if (i >= length) {
	        sb.append('/');
	        sb.append('>');
		} else {
			
	        sb.append('>');
			emitContentBodyMethod(sb, ja, i, length, e);
			sb.append('<');
	        sb.append('/');
			sb.append(tagName);
	        sb.append('>');
	    }
        return sb.toString();
    }
    
    /**
     * Reverse the JSONML transformation, making an XML text from a JSONObject.
     * The JSONObject must contain a "tagName" property. If it has children, 
     * then it must have a "childNodes" property containing an array of objects. 
     * The other properties are attributes with string values.
     * @param jo A JSONObject.
     * @return An XML string.
     * @throws JSONException
     */
    //_____________________________________________________________
    
    public static StringBuffer emitContentFor(int i, int len, Object e, StringBuffer sb,
    		JSONArray ja) {
    	for (i = 0; i < len; i += 1) {
		    e = ja.get(i);
		    if (e != null) {
		    	if (e instanceof String) {
		    		sb.append(XML.escape(e.toString()));
				} 
		    }
		}
    	return sb;
    }
    //____________________________________________________________
	public static String toString(JSONObject jo) throws JSONException {
	    StringBuffer sb = new StringBuffer();
	    Object		 e;
	    int          i;
	    JSONArray    ja;
	    String       k;
	    Iterator     keys;
	    int          len;
	    String       tagName;
	    String       v;
	
//Emit <tagName
	
		tagName = jo.optString("tagName");
		if (tagName == null) {
			return XML.escape(jo.toString());
		}
		XML.noSpace(tagName);
		tagName = XML.escape(tagName);
		sb.append('<');
		sb.append(tagName);
	
//Emit the attributes
	
        keys = jo.keys();
        sb = emitAttributesMethod(sb,v, k,jo, keys);   
		     	
//Emit content in body
	
		ja = jo.optJSONArray("childNodes");
		if (ja == null) {
	        sb.append('/');
	        sb.append('>');
		} else {
	        sb.append('>');
			len = ja.length();
			sb = emitContentFor(i,len, e, sb,  ja);
			
			sb.append('<');
	        sb.append('/');
			sb.append(tagName);
	        sb.append('>');
	    }
        return sb.toString();
    }
}