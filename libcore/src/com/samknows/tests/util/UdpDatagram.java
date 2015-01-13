package com.samknows.tests.util;

import java.nio.ByteBuffer;

public class UdpDatagram {
		private static final int PACKETSIZE = 16;
		private static final int SERVERTOCLIENTMAGIC = 0x00006000;
		private static final int CLIENTTOSERVERMAGIC = 0x00009000;

		private int datagramid;
		@SuppressWarnings("unused")
		private int starttimesec;
		@SuppressWarnings("unused")
		private int starttimeusec;
		private int magic;

		// When we make the "ping" we don't want to lose any time in memory
		// allocations, as much as possible should be ready (I miss structs...)
		byte[] arrayRepresentation;

		public static int getSize(){
			return PACKETSIZE;
		}
				
		public boolean isReady(int datagramId){
			return ( ( magic == UdpDatagram.CLIENTTOSERVERMAGIC || magic == UdpDatagram.SERVERTOCLIENTMAGIC ) && 
					   this.datagramid == datagramId);
		}
		
		public UdpDatagram(byte[] byteArray) {											/* Construct datagram object from raw buffer, usually on receipt */
			arrayRepresentation = byteArray;
			ByteBuffer bb = ByteBuffer.wrap(byteArray);
			datagramid = bb.getInt();
			starttimesec = bb.getInt();
			starttimeusec = bb.getInt();
			magic = bb.getInt();
		}

		public UdpDatagram(int datagramid) {											/* Construct datagram object from given ID number, usually on send */
			this.datagramid = datagramid;
			magic = CLIENTTOSERVERMAGIC;
			arrayRepresentation = new byte[PACKETSIZE];
			arrayRepresentation[0] = (byte) (datagramid >>> 24);
			arrayRepresentation[1] = (byte) (datagramid >>> 16);
			arrayRepresentation[2] = (byte) (datagramid >>> 8);
			arrayRepresentation[3] = (byte) (datagramid);
			arrayRepresentation[12] = (byte) (magic >>> 24);
			arrayRepresentation[13] = (byte) (magic >>> 16);
			arrayRepresentation[14] = (byte) (magic >>> 8);
			arrayRepresentation[15] = (byte) (magic);
		}

		public byte[] byteArray() {															/* Return datagram as a raw buffer for further processing */
			return arrayRepresentation;
		}

		public void setTime(long time) {														/* Embed time just before datagram sent into a datagram */
			int starttimesec = (int) (time / (int) 1e9);
			int starttimeusec = (int) ((time / (int) 1e3) % (int) 1e6);

			arrayRepresentation[4] = (byte) (starttimesec >>> 24);
			arrayRepresentation[5] = (byte) (starttimesec >>> 16);
			arrayRepresentation[6] = (byte) (starttimesec >>> 8);
			arrayRepresentation[7] = (byte) (starttimesec);
			arrayRepresentation[8] = (byte) (starttimeusec >>> 24);
			arrayRepresentation[9] = (byte) (starttimeusec >>> 16);
			arrayRepresentation[10] = (byte) (starttimeusec >>> 8);
			arrayRepresentation[11] = (byte) (starttimeusec);
		}
	}