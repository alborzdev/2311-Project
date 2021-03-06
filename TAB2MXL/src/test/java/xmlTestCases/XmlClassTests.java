package xmlTestCases;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sun.xml.fastinfoset.util.DuplicateAttributeVerifier.Entry;

import xmlClasses.Attributes;
import xmlClasses.Chain;
import xmlClasses.Clef;
import xmlClasses.Creator;
import xmlClasses.DrumPartWriter;
import xmlClasses.Identification;
import xmlClasses.Instrument;
import xmlClasses.Key;
import xmlClasses.Measure;
import xmlClasses.Notations;
import xmlClasses.Note;
import xmlClasses.Part;
import xmlClasses.PartWriter;
import xmlClasses.Part_List;
import xmlClasses.Pitch;
import xmlClasses.ScorePartwiseWriter;
import xmlClasses.Score_Part;
import xmlClasses.Score_Partwise;
import xmlClasses.StaffDetails;
import xmlClasses.StaffTuning;
import xmlClasses.Technical;
import xmlClasses.Time;
import xmlClasses.Work;

public class XmlClassTests {
	
	private Pitch p;
	private Note n;
	private Note n2;
	private Notations nots;
	private int voice;
	private Part part;
	private Part part2;
	private Measure measure;
	private Measure measure2;
	private StaffDetails sd;
	private Attributes att;
	
	private Chain chain;
	private int randomfret;
	private String TAB;
	
	@BeforeEach
    public void setUp() throws Exception {
        p = new Pitch("C", 4, 0);
        n = new Note(4, "quarter", p);
        nots = new Notations(new Technical(1,1));
        Technical tech = new Technical();
        n2 = new Note(4, "quarter", p, nots, voice, null,  null);
        part = new Part();
        part2 = new Part("1");
        Key k = new Key(0);
        Time t = new Time(4, 4);
        Clef c = new Clef("G", 2);
        Attributes att2 = new Attributes(1, k, t, c);
        Measure m = new Measure();
        ArrayList <Measure> measures = new ArrayList<Measure>();
        measures.add(m);
        Part p = new Part("P1", measures);
        Score_Part sp = new Score_Part("P1", "Music");
        Part_List pl = new Part_List(sp); 
        ArrayList <Creator> creators = new ArrayList<Creator>();
        creators.add(new Creator("composer", "Aidan Mozart")); 
        creators.add(new Creator("lyricist", "Its ya boy")); 
        Identification id = new Identification(creators);
        		
        Work w = new Work("Hot cross BUNS");
        
        Score_Partwise spw = new Score_Partwise(3.1, pl, p, id, w);  
        Random r = new Random();
        randomfret=r.nextInt(10);
        
        TAB = "e|-----------------|\n"+
        "B|-----------------|\n"+
        "G|-----------------|\n"+
        "D|-----------------|\n"+
        "A|-----------------|\n"+
        "E|-"+randomfret+"---------------|";
        
        chain = new Chain(TAB,"First Song", "Queen B", "Ludwig van Beethoven", 44,"C major","Guitar","TAB");
        
    }

    @Test
    public void testChangeStep() {
        assertEquals("C",p.getStep());
        p.setStep("G");
        assertEquals("G",p.getStep());
    }

    @Test //Test to make sure note object contains correct attributes
    public void testNote() {
        assertEquals("quarter", n.getType());
        assertEquals(4,n.getDuration());
        assertEquals(p,n.getPitch());
    }
	@Test 
	/**
	 * Testing second constructor for note with Notations and technical attributes
	 */
    public void testNote2() {
        assertEquals(nots, n2.getNotations());
        assertEquals(1, n2.getNotations().getTech().getFret());
        assertEquals(1, n2.getNotations().getTech().getString());
        //changing fret value
        n2.getNotations().getTech().setFret(2); 
        //checking to see if change occurred
        assertEquals(2, n2.getNotations().getTech().getFret());
        
        //changing string value
        n2.getNotations().getTech().setString(2); 
        //checking to see if change occurred
        assertEquals(2, n2.getNotations().getTech().getString());
    }
    
    @Test
    public void testPartConstructor() {
        assertNotSame(part, part2);
    }
    
    @Test
    /**
     * Testing a creation of multiple measures, comparing them
     * as well as their attributes
     */
    public void testMeasure() {
    	measure = new Measure();
    	assertNull(measure.getNote());
    	assertEquals(0, measure.getNumber());
    	assertNull(measure.getAttributes());
    	measure.setNumber(1);
    	assertTrue(measure.getNumber() == 1);
    	assertNotNull(measure.getNumber());
    	
    	//changed to work with new measure entries
    	Measure measureTwo = new Measure(1, new Attributes());
    	assertNull(measureTwo.getAttributes().getClef());
    	assertNotNull(measureTwo.getNote());
    	measureTwo.addNote(new Note(1, ""));
    	
    	assertNotEquals(measure, measure2);
    	
    	//int number, Attributes att, ArrayList<Note> note
    	//Measure m = new Measure(1,)
    }
    @Test
    /**
     * Testing the creation of staff details and staff tuning
     * Making sure set and get methods work properly
     */
    public void testStaffDetails() {
    	StaffTuning st = new StaffTuning();
    	assertNull(st.getTuningStep());
    	st.setTuningStep("E");
    	assertEquals("E", st.getTuningStep());
    	sd = new StaffDetails(6, new ArrayList<StaffTuning>());
    	assertEquals(6, sd.getStafflines());
    	
    	sd.getStaffTuning().add(st);
    	assertEquals("E", sd.getStaffTuning().get(0).getTuningStep());
    	
    	
    }
    @Test
    public void testInstrument() {
    	Instrument ins = new Instrument("Guitar");
    	Instrument ins2 = new Instrument("Drum");
    	Instrument ins3 = new Instrument("Bass");
    	assertEquals("Guitar",ins.getId());
    	assertEquals("Drum",ins2.getId());
    	assertEquals("Bass",ins3.getId());
    }
    
    @Test
    /**
     * Testing the score partwise writer
     * ensures that all arguments are properly set in a ScorePartwise
     */
    public void testSPW() {
    	ArrayList<String> drumKit = new ArrayList();
    	drumKit.add("Bass Kick");
    	ScorePartwiseWriter SPW = new ScorePartwiseWriter("Title", "LYRICIST" , "COMPOSER", part);
    	ScorePartwiseWriter SPW2 = new ScorePartwiseWriter("Title", "LYRICIST" , "COMPOSER", part, drumKit);
    	assertNotEquals(SPW, SPW2);
    	
    	assertEquals("Title", SPW.getScore_Partwise().getWork().getWorkTitle());
    	assertEquals("LYRICIST", SPW.getScore_Partwise().getId().getCreator(1).getName());
    	assertEquals("COMPOSER", SPW.getScore_Partwise().getId().getCreator(0).getName());
    	SPW.getScore_Partwise().setPart(part2);
    	assertNotEquals(part, SPW.getScore_Partwise().getPart());
    	assertNotEquals(SPW, SPW2);
    }
    
    @Test
    /**
     * Testing the score partwise writer
     * ensures that all arguments are properly set in a ScorePartwise
     */
    public void testPW() {
    	att = new Attributes();
    	PartWriter PW = new PartWriter();
    	PartWriter PW2 = new PartWriter();
    	assertNotEquals(PW, PW2);
    	
    	PW.getPart().setId("test");
    	assertEquals("test", PW.getPart().getID());
    	assertNotEquals(PW.getPart().getID(), PW2.getPart().getID());
    	
    	PW.nextMeasure();
    	PW.nextMeasure(att);
    	PW.nextMeasure(0, 0, 0, 0, "TAB", 0);
    	
    }
    @Test
    /**
     * Testing the Drum Part writer
     * ensures that all arguments are properly set in a DrumPart
     */
    public void testDPW() {
    	att = new Attributes();
    	DrumPartWriter DPW = new DrumPartWriter();
    	DrumPartWriter DPW2 = new DrumPartWriter();
    	assertNotEquals(DPW, DPW2);
    	
    	assertNotEquals(DPW.getDrumPart(), DPW2.getDrumPart());
    	DPW.nextBackup(16);
    	DPW.nextForward(16);
    	
    	DPW.nextMeasure();
    	DPW.nextMeasure(att);
    	DPW.nextDrumNote(0, "Type", "Test", 0, 0, "Test", "Test");
    	assertEquals(DPW.getDrumPart().getMeasure().get(0).getAttributes(), att);
    	
    	
    	
    	
    }
}
