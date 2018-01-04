import processing.core.*; 
import java.applet.*;  
 
import java.io.*; 
import java.net.*; 
import java.text.*; 
import java.util.*; 
import java.util.zip.*; 
import java.util.regex.*; 

//Provide Javadoc comments for each public class / interface.

/**
* PJDCC - Summary for class responsabilities.
*
* @author var-group
* @since 
* @version 
*/
public class elar001 extends PApplet {
	
	/**
	 * 
	 * @author utente
	 * @classe elarAttr
	 */
	private class ElarAttr{
		
		int width_default=800;
		int height_default=600;

		//colors zone
		int ab=255;
		int ac=242;
		int aa=204;
		int ad=47;
		int ae=125;
		int white_color = color(ab, ab, ab);//qui
		int yellow_color = color(ac, aa, ad);//qui
		int black_color = color(0,0,0);
		int red_color_01 = color(ab,0,ae);//qui
		int red_color_02 = color(ab,0,0);//qui
		int green_color = color(ae,ab,0);//qui
		int blue_color = color (0,0, ab);//qui

		//color button
		int color = 204;
		int button_gray_color=color(color);
		int color1= 255;
		int button_on_gray_color=color(color1);
		int button_press_gray_color=color(0);

		//size button
		int button_width=135;
		int button_height=35;
		int button_width_min=38;
		int button_height_min= 0;
		int button_height1 = 0;//mio

		//init button positions
		int button_x=10;
		int button_y=60;
		int button_separation=10;
		int button_x_min=20;
		int button_reviser=6;
		int button_x_lang=width_default-150;
		int button_y_lang=4;
		int button_lang_separation=5;
		int button_lang_adjustment=0;

		//que bot\u00f3n est\u00e1 seleccionado?
		boolean bos_en=false;
		boolean bos_es=false;
		boolean bos_gl=false;
		boolean bos_home=false;
		boolean bos_about_us=true;
		boolean bos_services=false;
		boolean bos_proyects=false;
		boolean bos_contact=false;

	}
	//as imaxes
	PImage img00;
	PImage img01;
	PImage img;
	
/*
v001

eLAR

A Bdunk & Wireless Galicia \u00b4s Project

-- Free Domotic Sistem --

*/


//imos a escribir todo en galego (menos os nomes das variables), despois si o liberamos facemos a traduccion


//data base SQL
//necesitmaos esta libreria para MySQL


int contador_borrar=0;

//a clase serial

Serial myPort;  // The serial port

//imos a usar fontes
PFont font;

//iniciamos as variables

MySQL msql;

Button b_en; 
Button b_es;
Button b_gl;

Check[] check;

Button[] panel;

boolean[] bos_panel;

String[] despription_panel;

//definimos as entradas dixitais 0,0
Bulb[] bulb;

Scrollbar[] scrollbar;
boolean[] draggingSlider;
int[] pos;

Rule rule;

//english
int lang = 0;

//welcome to home :-)
int section=0;

//other vars
int y = year(); //the year
int panel_number;
/**
 * documentazione javaDoc
 */
public void setup() {
  
  //mysql configuration  
   String user     = "elar";
   String pass     = "elar";
   String database = "elar";
   String server = "\"192.168.1.121\"";
   //String server = "10.10.1.157";

  msql = new MySQL( this, server, database, user, pass );
  msql.connect() ;
  
  if ( msql.connect() )
    {
        //CONFIGURATION
        msql.query( "SELECT * FROM configuration" );
        msql.next();
        
        width_default=msql.getInt( "screen_width" );
        height_default=msql.getInt( "screen_height" );
        println("width_default: "+width_default);
        println("height_default: "+height_default);
    } 
 
   size(width_default,height_default);
   
   //this looks better
   smooth();
   
   font=loadFont("Consolas-48.vlw"); 
   
   
   //cargamos todo o necesario
   //entradas dixitais
   
   
   
   //ler no porto serie
   //por si acaso fallase temos a lista para saber que porto e
   println(Serial.list());
   int af=19200;
   myPort = new Serial(this, Serial.list()[0], af);

   // read bytes into a buffer until you get a linefeed (ASCII 10):
   myPort.bufferUntil('\n');
   
   if ( msql.connect() )
    {
        //CONFIGURATION
        msql.query( " SELECT COUNT( * ) AS panel_number FROM panels " );
        msql.next();
        
        //number of panels
        panel_number=msql.getInt("panel_number");
        panel_number++;
        println("panel_number: "+panel_number);

   } 
   panel = new Button[panel_number];
   
   bos_panel = new boolean[panel_number];
   
   for (int i = 0; i < panel.length; i++) {
     bos_panel[i]=false; 
   }
   
   despription_panel = new String[panel_number];
   
   numberPanels(); 

   check = new Check[1];
   int ag=200;
   int ah=240;
   int ai=50;
   check[0] = new Check(ag, ah, ai, color(0));
   
   scrollbar = new Scrollbar[1];
   draggingSlider = new boolean[1];
   int ba=350;
   int bb=80;
   int bc=10; 
   scrollbar[0] = new Scrollbar(ag, ba, bb, bc, -40.0f, 50.0f);
   draggingSlider[0]=false;

   
}

/**
 * documentazione javaDoc
 */
public void draw() {
	int ai=50;
	int bc=10;
	int v=33;
  rule = new Rule();
  
  background(yellow_color);
  fill(white_color);
  textFont(font);
  
  //header
  fill(0);
  stroke(0);
  rect(0,0,width_default,ai);
  int text=30;
  textSize(text);
  fill(white_color);
  text(l_page_title[lang],bc,v); 
  
  //slogan
  int text1=18;
  textSize(text1);
  int h=250;
  int z=30;
  text("["+l_slogan[lang]+"]", h, z); 
  
  //the footer
  fill(black_color);
  int text2=11;
  textSize(text2);
  text(l_footer[lang]+" "+y,bc,height_default-20);
  
  //lang buttons
  textSize(text1);
  
  
  
  b_en = new Button(button_x_lang, button_y_lang+1*(button_lang_separation), button_width_min, button_height_min, button_gray_color, button_on_gray_color, button_press_gray_color, "EN", bos_en, button_lang_adjustment);
  b_gl = new Button(button_x_lang+1*(button_lang_separation+button_width_min), button_y_lang+1*(button_lang_separation), button_width_min, button_height_min, button_gray_color, button_on_gray_color, button_press_gray_color, "GL", bos_gl, button_lang_adjustment);
  b_es = new Button(button_x_lang+2*(button_lang_separation+button_width_min), button_y_lang+1*(button_lang_separation), button_width_min, button_height_min, button_gray_color, button_on_gray_color, button_press_gray_color, "ES", bos_es, button_lang_adjustment);
  
  buttonsOn();
   
   switch (section) { 
     case 0: //about us
      show_about_us();
     break;
     default: 
    	 System.out.println(" ");
    	 break;
    }
  int u= 360;
  int xa=270;
  int yy=350;  
  int xx=100;
  int ciuffo=80;//y
  int x=200;
  text(despription_panel[section], x, ciuffo);//y
  
  initButtons();
  
  bulb = new Bulb[1];
  bulb[0] = new Bulb(false,"Bombilla tipo",x, xx, ciuffo, ciuffo);//y
  
  if (section==1) {
    bulb[0].display();
    check[0].display();
    boolean pos0=check[0].checked;
    text(check[0].escribe(pos0),yy, xa);
    pos = new int[1];
    pos[0] = PApplet.parseInt(scrollbar[0].getPos()); 
    scrollbar[0].update(mouseX, mouseY); 
    scrollbar[0].display(); 
    text(nf(pos[0], 3)+" \u00baC", yy, u);
  }
  
  if (section==2) {
     if ( msql.connect() )
    {
        //CONFIGURATION
        msql.query( "SELECT * FROM devices WHERE device_type=0 AND io_type=0" );
        
        int h = 120;
        int i=0;
        while (msql.next()) {
        
          int id_device=msql.getInt( "id_device") ;
          double default_value=msql.getDouble("default_value");
          String name_device=msql.getString( "name") ;  
          String description_device=msql.getString( "description") ;  
          text(name_device+" "+id_device+" "+description_device+" valor: "+default_value,x,120+h*i);
          
          i++;
        }
        
    }
  }  
}

public void mousePressed() { 
  if (b_en.press() == true) { lang = 0; } 
  if (b_gl.press() == true) { lang = 1; }
  if (b_es.press() == true) { lang = 2; }
  
  for (int i = 0; i < panel.length; i++) {
    if (panel[i].press() == true) { section = i; }
  }
  
  check[0].press(mouseX, mouseY);
  scrollbar[0].press(mouseX, mouseY);  
  
} 

public void mouseReleased() { 
  b_en.release();
  b_es.release();
  b_gl.release();
  for (int i = 0; i < panel.length; i++) {
    panel[i].release(); 
  }
  scrollbar[0].release(); 
  draggingSlider[0]=false;


}

public void initButtons() {
  b_en.update();
  b_es.update();
  b_gl.update();
  b_en.display();
  b_es.display();
  b_gl.display();
  
  for (int i = 0; i < panel.length; i++) {
    panel[i].update(); 
    panel[i].display();
  }
  
}

public void buttonsOn() {
   switch (lang) { 
     case 0:
      bos_en=true;
      bos_es=false;
      bos_gl=false;
     break;
     case 1:
      bos_en=false;
      bos_es=false;
      bos_gl=true;
     break;
     case 2:
      bos_en=false;
      bos_es=true;
      bos_gl=false;
     break;
     default: 
    	 System.out.println(" ");
    	 break;
   }
   
   bos_panel[section]=true;
   
 }


public int numberPanels () {
   if ( msql.connect() )
    {
        //CONFIGURATION
        msql.query( "SELECT * FROM panels" );
               
        int i=0;

        panel[i] = new Button(button_x, button_y+i*(button_separation+button_height), button_width, button_height, button_gray_color, button_on_gray_color, button_press_gray_color, l_about_us[lang], bos_panel[i], button_reviser);
         
         despription_panel[i]="";
        
        while (msql.next()) {
          
          i++;
          
          int id_panel=msql.getInt( "id_panel" );
          String name=msql.getString( "name") ;
          println("id_panel:"+id_panel);
        
          panel[i] = new Button(button_x, button_y+i*(button_separation+button_height), button_width, button_height, button_gray_color, button_on_gray_color, button_press_gray_color, name, bos_panel[i], button_reviser);
          
          despription_panel[i]=msql.getString( "description");
       
      }
        
              
       return 1;

   } else {
     
       return 0; 
       
   }
}


public void serialEvent(Serial myPor) { 
  // read the serial buffer:
  String myString = myPort.readStringUntil('\n');
  // if you got any bytes other than the linefeed:
    myString = trim(myString);
 
   println(myString);
 
 /*
    // split the string at the commas
    // and convert the sections into integers:
    int sensors[] = int(split(myString, ','));

    // print out the values you got:
    for (int sensorNum = 0; sensorNum < sensors.length; sensorNum++) {
      print("Sensor " + sensorNum + ": " + sensors[sensorNum] + "\t"); 
    }
    // add a linefeed after all the sensor values are printed:
    println();
    if (sensors.length > 1) {
      xpos = map(sensors[0], 0,1023,0,width);
      ypos = map(sensors[1], 0,1023,0,height);
      fgcolor = sensors[2];
    }
    // send a byte to ask for more data:
    myPort.write("A");
   */
  }





public void show_about_us() {
	int text3=16;
   textSize(text3);
   fill(white_color);

   img00=loadImage("logo_wg.jpg");
   img01=loadImage("logo_bdunk.jpg");
   int gg=170; int aiu=120; int yu= 175; int hg=75;
   int hh=60; int uu=190; int ii=35;
   image(img00, gg, hh, uu, ii);
   image(img01, gg, aiu, yu, hg);
   int sa=220;
   text(l_text_about_us[lang], gg, sa); 
    
}

/**
 * documentazione javaDoc
 * @author vincy
 *
 */
private class Bulb { 
  boolean o_o;
  String des;
  int x;
  int y;
  int h;
  int w;
  Bulb (boolean on_off, String description, int xp, int yp, int hp, int wp) { 
    o_o = on_off; 
    des = description;  
    x=xp;
    y=yp;
    h=hp;
    w=wp;
  } 
  /**
   * documentazione javaDoc
   */
  public void display () {
  
      if (o_o==false) {
      
        img=loadImage("bulb_off.jpg");
               
      } else {
      
        img=loadImage("bulb_on.jpg");
        
      }
      
      image(img, x, y, w, h);
 
      text(des,x,y+h+20);
  
  }
  
}

/**
 * documentazione javaDoc
 * @author vincy
 *
 */
private class Button { 
  int x;
  int y; // The x- and y-coordinates 
  int xsize; // Dimension (width) 
  int ysize; // Dimension (width) 
  int baseGray; // Default gray value 
  int overGray; // Value when mouse is over the button 
  int pressGray; // Value when mouse is over and pressed 
  boolean over = false; // True when the mouse is over 
  boolean pressed = false; // True when the mouse is over and pressed 
  String texto_boton;
  boolean on_section;
  int corrector;
  Button(int xp, int yp, int w, int h, int b, int o, int p, String t, boolean os, int corr) { 
    x = xp; 
    y = yp; 
    xsize = w; 
    ysize = h;
    baseGray = b; 
    overGray = o; 
    pressGray = p; 
    texto_boton =t;
    on_section=os;
    corrector=corr;
    
  } 
// Updates the over field every frame 
  /**
   * documentazione javaDoc
   */
  public void update() { 
    if ((mouseX >= x) && (mouseX <= x+xsize) && 
        (mouseY >= y) && (mouseY <= y+ysize)) { 
      over = true; 
    } else { 
      over = false; 
    } 
  }
  
  /**
   * documentazione javaDoc
   * @return
   */
  public boolean press() { 
    if (over == true) { 
      pressed = true; 
      return true; 
    } else { 
      return false; 
    } 
  } 
  public void release() { 
    pressed = false; // Set to false when the mouse is released 
  } 
  public void display() { 
    if (pressed == true) { 
      fill(pressGray); 
    } else if (over == true) { 
      fill(overGray); 
    } else { 
      fill(baseGray); 
    } 
    if (on_section == true) {
      fill(overGray);
    }
    int a=255;
    stroke(a); 
    rect(x, y, xsize, ysize); 
    fill(0);
    text(texto_boton, x+5, y+(ysize/2)+corrector);
  } 
} 

/**
 * documentazione JavaDoc
 * @author vincy
 *
 */
private class Check { 
  int x;
  int y; // The x- and y-coordinates 
  int size; // Dimension (width and height) 
  int baseGray; // Default gray value 
  boolean checked = false; // True when the check box is selected 
  
  //vincenza String
  String[] l_page_title={"eLAR Project", "Proxecto eLAR", "Proyecto eLAR"};
  String[] l_slogan={"Free Domotic System", "Sistema Dom\u00f3tico Libre", "Sistema Dom\u00f3tico Libre"};
  String[] l_footer={
    "eLAR [visit hardprocessing.org] | this panel is make with processing [visit processing.org] | (c) wireless galicia, bdunk",
    "eLAR [visite hardprocessing.org] | este panel est\u00e1 feito con processing [visite processing.org] | (c) wireless galicia, bdunk",
    "eLAR [visite hardprocessing.org] | este panel est\u00e1 hecho con processing [visite processing.org] | (c) wireless galicia, bdunk"};
  String[] l_about_us={"About us", "Sobre n\u00f3s", "Acerca de"};
  String[] l_text_about_us={"We are a group of restless minds", "Somos un grupo de mentes inquietas", "Somos un grupo de mentes inquietas"};
  
  Check(int xp, int yp, int s, int b) { 
    x = xp; 
    y = yp; 
    size = s; 
    baseGray = b; 
  } 
// Updates the boolean variable checked 
  /**
   * documentazione javaDoc
   * @param mx
   * @param my
   */
  public void press(float mx, float my) { 
    if ((mx >= x) && (mx <= x+size) && (my >= y) && (my <= y+size)) { 
      checked = !checked; // Toggle the check box on and off 
    } 
  } 

  /**
   * documentazione javaDoc
   */
  public void display() { 
	  int b= 255;
    stroke(b); 
    fill(baseGray); 
    rect(x, y, size, size); 
    if (checked == true) { 
      line(x, y, x+size, y+size); 
      line(x+size, y, x, y+size); 
    } 
  } 
  
  public String escribe(boolean posicion) {
   String resultado="";
   if (posicion==true) {
    resultado="ON";
   } else {
    resultado="OFF";
   }
  return resultado; 
  }
} 
//l_test={"", "", ""},


public static void switchMethod(int stop, int default_value, float value, int operator) {//mio
	 switch (operator) {
     case 0:
        println("Maior");
        if (stop==0 && default_value>value) {
          println("vamos");
        }
             
     break;
     default:
       println("Algo malo pasou");
     break; 
    }
}
public static void whileMethod() {//mio
	while (msql.next()) {
        int id_rule=msql.getInt("id_rule");
        println("id_rule: "+id_rule);
        println("name: "+msql.getString("name"));
        println("name: "+msql.getString("description"));
        
         //entonces comprobamos si ten accions
         msql.query( "SELECT * FROM conditions WHERE id_rule="+id_rule );
         
         //por defecto a regra non se cumpre
         int stop=0;
         while (msql.next()) {
           
           int id_device=msql.getInt("id_device");
           int operator=msql.getInt("operator");
           float value=msql.getFloat("value");
           println("id_condition: "+msql.getInt("id_condition"));
           println("id_device: "+id_device);
           println("operator: "+operator);
           println("value: "+value);
           
           //recollemos o valor actual do sensor que nos mande
           msql.query( "SELECT default_value FROM devices WHERE id_device="+id_device );
           int default_value=0;
           if (msql.next()) {
            default_value=msql.getInt("default_value");
            println(default_value);
           } else {
            stop=1;
           } 
          
           switchMethod(stop,default_value, value, operator);
          
           
         }  
      }
}
public static void if2Method() {//mio
	if ( msql.connect() ){
      //CONFIGURATION
      msql.query( "SELECT * FROM rules" );
      whileMethod();
      
     
  } 
}
public static void ifMethod() {//mio
	if (contador_borrar==0) { //despois elmininar este if agora imos so facer probas
	     contador_borrar++;
	     
	     println("vamos coa regras");
	   if2Method();
	     
	 
	      
	   }
}

/**
 * documentazione javaDoc
 * @author vincy
 *
 */
private class Rule {
  
   Rule() {
   
   ifMethod();
   
   
   
   }
   
   
  
}

/**
 * documentazione javaDoc
 * @author vincy
 *
 */
private class Scrollbar { 
  int x;
  int y; // The x- and y-coordinates 
  float sw;
  float sh;  // Width and height of scrollbar 
  float pos; // Position of thumb 
  float posMin;
  float posMax; // Max and min values of thumb 
  boolean rollover; // True when the mouse is over 
  boolean locked; // True when its the active scrollbar 
  float minVal;
  float maxVal;  // Min and max values for the thumb 
  Scrollbar (int xp, int yp, int w, int h, float miv, float mav) { 
    x = xp; 
    y = yp; 
    sw = w; 
    sh = h; 
    minVal = miv; 
    maxVal = mav; 
    pos = x + sw/2 - sh/2; 
    posMin = x; 
    posMax = x + sw - sh; 
  } 
// Updates the over boolean and the position of the thumb 
  /**
   * documentazione javaDoc
   * @param mx
   * @param my
   */
  public void update(int mx, int my) { 
	  boolean pp=over(mx, my);//mio
    if (pp == true) { 
      rollover = true; 
    } else { 
      rollover = false; 
    } 
    if (locked == true) { 
      pos = constrain(mx-sh/2, posMin, posMax); 
    } 
  } 
// Locks the thumb so the mouse can move off and still update 
  
  /**
   * documentazione javadoc
   * @param mx
   * @param my
   */
  public void press(int mx, int my) { 
    if (rollover == true) { 
      locked = true; 
    } else { 
      locked = false; 
    } 
  } 
 // Resets the scrollbar to neutral 
  public void release() { 
    locked = false; 
  } 
// Returns true if the cursor is over the scrollbar 
  public boolean over(int mx, int my) { 
    if ((mx > x) && (mx < x+sw) && (my > y) && (my < y+sh)) { 
      return true; 
    } else { 
      return false; 
    } 
  } 
// Draws the scrollbar to the screen 
  public void display() { 
	  int c=255;
    fill(c); 
    rect(x, y, sw, sh); 
    if ((rollover==true) || (locked==true)) { 
    	int d=0;
      fill(d); 
    } else { 
    	int e=102;
      fill(e); 
    } 
    rect(pos, y, sh, sh); 
  } 
// Returns the current value of the thumb 
  public float getPos() { 
    float scalar = sw/(sw-sh); 
    float ratio = (pos - x) * scalar; 
    float offset = minVal + (ratio/sw * (maxVal-minVal)); 
    return offset; 
  } 
}
  static public void main(String[] args) {
    PApplet.main(new String[] { "--present", "--bgcolor=#666666", "--stop-color=#cccccc", "elar001" });
  }
}
