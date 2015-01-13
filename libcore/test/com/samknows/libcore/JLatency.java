package com.samknows.libcore;

import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;




import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import com.samknows.tests.Latency;
import com.samknows.tests.Param;


@RunWith(RobolectricTestRunner.class)
public class JLatency {
	
	/*	<param name="target" value="$closest"/>
	<param name="port" value="6000"/>
	<param name="interPacketTime" value="500000"/>
	<param name="delayTimeout" value="2000000"/>
	<param name="numberOfPackets" value="60"/>
	<param name="percentile" value="100"/>
	<param name="maxTime" value="30000000"/>
*/	

	List<Param> params = new ArrayList<Param>();

	public JLatency(){
		params.add(new Param("target", "n1-the1.samknows.com"));
		params.add(new Param("port", "6000"));
		params.add(new Param("interPacketTime", "500000"));
		params.add(new Param("delayTimeout", "2000000"));
		params.add(new Param("numberOfPackets", "60"));
		params.add(new Param("percentile", "100"));
		params.add(new Param("maxTime", "30000000"));
		
		latency = new Latency(params);
	}
	
	
	Latency latency = null;
	//* In order to run calibration test make relevant fields and methods of the Latency class public *//
	/*@Test
	public final void testAggregates(){
		latency.results = new long[400];
		
		latency.percentile = 92.0;
		latency.recvPackets = 363;
		
		{			
			latency.results[ 0 ] = 660 ;latency.results[ 1 ] = 656 ;latency.results[ 2 ] = 656 ;latency.results[ 3 ] = 686 ;latency.results[ 4 ] = 658 ;latency.results[ 5 ] = 657 ;latency.results[ 6 ] = 656 ;latency.results[ 7 ] = 657 ;latency.results[ 8 ] = 658 ;latency.results[ 9 ] = 673 ;latency.results[ 10 ] = 656 ;latency.results[ 11 ] = 655 ;latency.results[ 12 ] = 659 ;latency.results[ 13 ] = 658 ;latency.results[ 14 ] = 664 ;latency.results[ 15 ] = 659 ;latency.results[ 16 ] = 660 ;latency.results[ 17 ] = 656 ;latency.results[ 18 ] = 659 ;latency.results[ 19 ] = 658 ;latency.results[ 20 ] = 667 ;latency.results[ 21 ] = 663 ;latency.results[ 22 ] = 663 ;latency.results[ 23 ] = 661 ;latency.results[ 24 ] = 661 ;latency.results[ 25 ] = 667 ;latency.results[ 26 ] = 660 ;latency.results[ 27 ] = 659 ;latency.results[ 28 ] = 663 ;latency.results[ 29 ] = 673 ;latency.results[ 30 ] = 663 ;latency.results[ 31 ] = 657 ;latency.results[ 32 ] = 663 ;latency.results[ 33 ] = 657 ;latency.results[ 34 ] = 656 ;latency.results[ 35 ] = 656 ;latency.results[ 36 ] = 663 ;latency.results[ 37 ] = 660 ;latency.results[ 38 ] = 656 ;latency.results[ 39 ] = 667 ;latency.results[ 40 ] = 662 ;latency.results[ 41 ] = 656 ;latency.results[ 42 ] = 656 ;latency.results[ 43 ] = 659 ;latency.results[ 44 ] = 661 ;latency.results[ 45 ] = 658 ;latency.results[ 46 ] = 665 ;latency.results[ 47 ] = 673 ;latency.results[ 48 ] = 668 ;latency.results[ 49 ] = 658 ;latency.results[ 50 ] = 659 ;latency.results[ 51 ] = 655 ;latency.results[ 52 ] = 658 ;latency.results[ 53 ] = 664 ;latency.results[ 54 ] = 656 ;latency.results[ 55 ] = 664 ;latency.results[ 56 ] = 660 ;latency.results[ 57 ] = 668 ;latency.results[ 58 ] = 670 ;latency.results[ 59 ] = 657 ;latency.results[ 60 ] = 656 ;latency.results[ 61 ] = 657 ;latency.results[ 62 ] = 664 ;latency.results[ 63 ] = 663 ;latency.results[ 64 ] = 658 ;latency.results[ 65 ] = 656 ;latency.results[ 66 ] = 660 ;latency.results[ 67 ] = 665 ;latency.results[ 68 ] = 657 ;latency.results[ 69 ] = 656 ;latency.results[ 70 ] = 661 ;latency.results[ 71 ] = 658 ;latency.results[ 72 ] = 662 ;latency.results[ 73 ] = 656 ;latency.results[ 74 ] = 673 ;latency.results[ 75 ] = 656 ;latency.results[ 76 ] = 660 ;latency.results[ 77 ] = 664 ;latency.results[ 78 ] = 663 ;latency.results[ 79 ] = 661 ;latency.results[ 80 ] = 657 ;latency.results[ 81 ] = 658 ;latency.results[ 82 ] = 659 ;latency.results[ 83 ] = 657 ;latency.results[ 84 ] = 656 ;latency.results[ 85 ] = 655 ;latency.results[ 86 ] = 667 ;latency.results[ 87 ] = 665 ;latency.results[ 88 ] = 660 ;latency.results[ 89 ] = 658 ;latency.results[ 90 ] = 668 ;latency.results[ 91 ] = 660 ;latency.results[ 92 ] = 656 ;latency.results[ 93 ] = 668 ;latency.results[ 94 ] = 659 ;latency.results[ 95 ] = 656 ;latency.results[ 96 ] = 656 ;latency.results[ 97 ] = 667 ;latency.results[ 98 ] = 659 ;latency.results[ 99 ] = 673 ;latency.results[ 100 ] = 666 ;latency.results[ 101 ] = 656 ;latency.results[ 102 ] = 660 ;latency.results[ 103 ] = 675 ;latency.results[ 104 ] = 660 ;latency.results[ 105 ] = 656 ;latency.results[ 106 ] = 659 ;latency.results[ 107 ] = 661 ;latency.results[ 108 ] = 659 ;latency.results[ 109 ] = 658 ;latency.results[ 110 ] = 659 ;latency.results[ 111 ] = 656 ;latency.results[ 112 ] = 660 ;latency.results[ 113 ] = 656 ;latency.results[ 114 ] = 663 ;latency.results[ 115 ] = 658 ;latency.results[ 116 ] = 663 ;latency.results[ 117 ] = 670 ;latency.results[ 118 ] = 657 ;latency.results[ 119 ] = 665 ;latency.results[ 120 ] = 659 ;latency.results[ 121 ] = 656 ;latency.results[ 122 ] = 658 ;latency.results[ 123 ] = 660 ;latency.results[ 124 ] = 661 ;latency.results[ 125 ] = 660 ;latency.results[ 126 ] = 662 ;latency.results[ 127 ] = 659 ;latency.results[ 128 ] = 660 ;latency.results[ 129 ] = 659 ;latency.results[ 130 ] = 664 ;latency.results[ 131 ] = 663 ;latency.results[ 132 ] = 662 ;latency.results[ 133 ] = 656 ;latency.results[ 134 ] = 656 ;latency.results[ 135 ] = 664 ;latency.results[ 136 ] = 664 ;latency.results[ 137 ] = 655 ;latency.results[ 138 ] = 657 ;latency.results[ 139 ] = 657 ;latency.results[ 140 ] = 664 ;latency.results[ 141 ] = 656 ;latency.results[ 142 ] = 655 ;latency.results[ 143 ] = 665 ;latency.results[ 144 ] = 669 ;latency.results[ 145 ] = 663 ;latency.results[ 146 ] = 657 ;latency.results[ 147 ] = 663 ;latency.results[ 148 ] = 658 ;latency.results[ 149 ] = 657 ;latency.results[ 150 ] = 658 ;latency.results[ 151 ] = 662 ;latency.results[ 152 ] = 663 ;latency.results[ 153 ] = 660 ;latency.results[ 154 ] = 674 ;latency.results[ 155 ] = 669 ;latency.results[ 156 ] = 681 ;latency.results[ 157 ] = 659 ;latency.results[ 158 ] = 666 ;latency.results[ 159 ] = 679 ;latency.results[ 160 ] = 665 ;latency.results[ 161 ] = 669 ;latency.results[ 162 ] = 656 ;latency.results[ 163 ] = 661 ;latency.results[ 164 ] = 660 ;latency.results[ 165 ] = 662 ;latency.results[ 166 ] = 656 ;latency.results[ 167 ] = 659 ;latency.results[ 168 ] = 659 ;latency.results[ 169 ] = 666 ;latency.results[ 170 ] = 666 ;latency.results[ 171 ] = 661 ;latency.results[ 172 ] = 663 ;latency.results[ 173 ] = 663 ;latency.results[ 174 ] = 666 ;latency.results[ 175 ] = 660 ;latency.results[ 176 ] = 673 ;latency.results[ 177 ] = 658 ;latency.results[ 178 ] = 676 ;latency.results[ 179 ] = 659 ;latency.results[ 180 ] = 656 ;latency.results[ 181 ] = 661 ;latency.results[ 182 ] = 662 ;latency.results[ 183 ] = 659 ;latency.results[ 184 ] = 675 ;latency.results[ 185 ] = 656 ;latency.results[ 186 ] = 658 ;latency.results[ 187 ] = 664 ;latency.results[ 188 ] = 665 ;latency.results[ 189 ] = 655 ;latency.results[ 190 ] = 660 ;latency.results[ 191 ] = 686 ;latency.results[ 192 ] = 655 ;latency.results[ 193 ] = 658 ;latency.results[ 194 ] = 658 ;latency.results[ 195 ] = 669 ;latency.results[ 196 ] = 665 ;latency.results[ 197 ] = 687 ;latency.results[ 198 ] = 655 ;latency.results[ 199 ] = 663 ;latency.results[ 200 ] = 658 ;latency.results[ 201 ] = 668 ;latency.results[ 202 ] = 672 ;latency.results[ 203 ] = 663 ;latency.results[ 204 ] = 673 ;latency.results[ 205 ] = 665 ;latency.results[ 206 ] = 660 ;latency.results[ 207 ] = 663 ;latency.results[ 208 ] = 667 ;latency.results[ 209 ] = 673 ;latency.results[ 210 ] = 657 ;latency.results[ 211 ] = 656 ;latency.results[ 212 ] = 656 ;latency.results[ 213 ] = 663 ;latency.results[ 214 ] = 656 ;latency.results[ 215 ] = 692 ;latency.results[ 216 ] = 658 ;latency.results[ 217 ] = 658 ;latency.results[ 218 ] = 669 ;latency.results[ 219 ] = 659 ;latency.results[ 220 ] = 664 ;latency.results[ 221 ] = 658 ;latency.results[ 222 ] = 657 ;latency.results[ 223 ] = 663 ;latency.results[ 224 ] = 663 ;latency.results[ 225 ] = 669 ;latency.results[ 226 ] = 666 ;latency.results[ 227 ] = 658 ;latency.results[ 228 ] = 667 ;latency.results[ 229 ] = 665 ;latency.results[ 230 ] = 664 ;latency.results[ 231 ] = 655 ;latency.results[ 232 ] = 660 ;latency.results[ 233 ] = 666 ;latency.results[ 234 ] = 656 ;latency.results[ 235 ] = 657 ;latency.results[ 236 ] = 666 ;latency.results[ 237 ] = 658 ;latency.results[ 238 ] = 663 ;latency.results[ 239 ] = 661 ;latency.results[ 240 ] = 656 ;latency.results[ 241 ] = 659 ;latency.results[ 242 ] = 656 ;latency.results[ 243 ] = 667 ;latency.results[ 244 ] = 667 ;latency.results[ 245 ] = 655 ;latency.results[ 246 ] = 656 ;latency.results[ 247 ] = 663 ;latency.results[ 248 ] = 667 ;latency.results[ 249 ] = 656 ;latency.results[ 250 ] = 655 ;latency.results[ 251 ] = 658 ;latency.results[ 252 ] = 660 ;latency.results[ 253 ] = 657 ;latency.results[ 254 ] = 655 ;latency.results[ 255 ] = 656 ;latency.results[ 256 ] = 662 ;latency.results[ 257 ] = 656 ;latency.results[ 258 ] = 668 ;latency.results[ 259 ] = 657 ;latency.results[ 260 ] = 658 ;latency.results[ 261 ] = 656 ;latency.results[ 262 ] = 656 ;latency.results[ 263 ] = 657 ;latency.results[ 264 ] = 668 ;latency.results[ 265 ] = 663 ;latency.results[ 266 ] = 663 ;latency.results[ 267 ] = 664 ;latency.results[ 268 ] = 656 ;latency.results[ 269 ] = 656 ;latency.results[ 270 ] = 656 ;latency.results[ 271 ] = 655 ;latency.results[ 272 ] = 658 ;latency.results[ 273 ] = 660 ;latency.results[ 274 ] = 663 ;latency.results[ 275 ] = 657 ;latency.results[ 276 ] = 659 ;latency.results[ 277 ] = 665 ;latency.results[ 278 ] = 664 ;latency.results[ 279 ] = 664 ;latency.results[ 280 ] = 658 ;latency.results[ 281 ] = 655 ;latency.results[ 282 ] = 656 ;latency.results[ 283 ] = 663 ;latency.results[ 284 ] = 664 ;latency.results[ 285 ] = 660 ;latency.results[ 286 ] = 659 ;latency.results[ 287 ] = 658 ;latency.results[ 288 ] = 656 ;latency.results[ 289 ] = 657 ;latency.results[ 290 ] = 658 ;latency.results[ 291 ] = 658 ;latency.results[ 292 ] = 663 ;latency.results[ 293 ] = 663 ;latency.results[ 294 ] = 656 ;latency.results[ 295 ] = 664 ;latency.results[ 296 ] = 659 ;latency.results[ 297 ] = 656 ;latency.results[ 298 ] = 657 ;latency.results[ 299 ] = 660 ;latency.results[ 300 ] = 658 ;latency.results[ 301 ] = 659 ;latency.results[ 302 ] = 663 ;latency.results[ 303 ] = 657 ;latency.results[ 304 ] = 666 ;latency.results[ 305 ] = 660 ;latency.results[ 306 ] = 660 ;latency.results[ 307 ] = 658 ;latency.results[ 308 ] = 656 ;latency.results[ 309 ] = 663 ;latency.results[ 310 ] = 658 ;latency.results[ 311 ] = 666 ;latency.results[ 312 ] = 663 ;latency.results[ 313 ] = 656 ;latency.results[ 314 ] = 666 ;latency.results[ 315 ] = 666 ;latency.results[ 316 ] = 657 ;latency.results[ 317 ] = 659 ;latency.results[ 318 ] = 659 ;latency.results[ 319 ] = 661 ;latency.results[ 320 ] = 679 ;latency.results[ 321 ] = 660 ;latency.results[ 322 ] = 656 ;latency.results[ 323 ] = 662 ;latency.results[ 324 ] = 663 ;latency.results[ 325 ] = 668 ;latency.results[ 326 ] = 656 ;latency.results[ 327 ] = 657 ;latency.results[ 328 ] = 655 ;latency.results[ 329 ] = 660 ;latency.results[ 330 ] = 657 ;latency.results[ 331 ] = 664 ;latency.results[ 332 ] = 656 ;latency.results[ 333 ] = 662 ;latency.results[ 334 ] = 667 ;latency.results[ 335 ] = 658 ;latency.results[ 336 ] = 748 ;latency.results[ 337 ] = 671 ;latency.results[ 338 ] = 666 ;latency.results[ 339 ] = 663 ;latency.results[ 340 ] = 656 ;latency.results[ 341 ] = 659 ;latency.results[ 342 ] = 655 ;latency.results[ 343 ] = 658 ;latency.results[ 344 ] = 658 ;latency.results[ 345 ] = 656 ;latency.results[ 346 ] = 671 ;latency.results[ 347 ] = 657 ;latency.results[ 348 ] = 656 ;latency.results[ 349 ] = 661 ;latency.results[ 350 ] = 670 ;latency.results[ 351 ] = 659 ;latency.results[ 352 ] = 674 ;latency.results[ 353 ] = 663 ;latency.results[ 354 ] = 663 ;latency.results[ 355 ] = 669 ;latency.results[ 356 ] = 661 ;latency.results[ 357 ] = 655 ;latency.results[ 358 ] = 660 ;latency.results[ 359 ] = 662 ;latency.results[ 360 ] = 659 ;latency.results[ 361 ] = 687 ;latency.results[ 362 ] = 666 ;		
		}
		
		int res = latency.getNumOfResults();
		assertTrue( res == 334 );
		
		latency.getAverage();
		assertTrue( latency.averageNanoseconds - 660.1167664671 < Math.abs(0.00000001));
				
		latency.getStdDev();
		assertTrue( latency.stddeviationNanoseconds - 3.7960173961 < Math.abs(0.00000001));
	}*/
	
	
	@Test
	public final void testIsReady() { assertTrue(latency.isReady());}
	
	@Test
	public final void testGetStringID() { assertTrue ( latency.getStringID() == "JUDPLATENCY" );}

	@Test
	public final void testRun(){ latency.run(); }
	
	@Test
	public final void testIsSuccessful() { assertTrue ( latency.isSuccessful() ); }

	@Test
	public final void testGetHumanReadableResult() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetResults() {
		fail("Not yet implemented"); // TODO
	}

	
	@Test
	public final void testGetNetUsage() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetProgress() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetPacketSize() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testLatencyListOfParam() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetInfo() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetTarget() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetValue() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetDimension() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testGetType() {
		fail("Not yet implemented"); // TODO
	}

}
