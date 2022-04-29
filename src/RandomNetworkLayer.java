// =============================================================================
// IMPORTS

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
// =============================================================================
import java.util.logging.Logger;



// =============================================================================
/**
 * @file   RandomNetworkLayer.java
 * @author Scott F. Kaplan (sfkaplan@cs.amherst.edu)
 * @date   April 2022
 *
 * A network layer that perform routing via random link selection.
 */
public class RandomNetworkLayer extends NetworkLayer {
// =============================================================================



    // =========================================================================
    // PUBLIC METHODS
    // =========================================================================

    Logger logger = Logger.getLogger(RandomNetworkLayer.class.getName());

    // =========================================================================
    /**
     * Default constructor.  Set up the random number generator.
     */
    public RandomNetworkLayer () {

	random = new Random();

    } // RandomNetworkLayer ()
    // =========================================================================

    

    // =========================================================================
    /**
     * Create a single packet containing the given data, with header that marks
     * the source and destination hosts.
     *
     * @param destination The address to which this packet is sent.
     * @param data        The data to send.
     * @return the sequence of bytes that comprises the packet.
     */
    protected byte[] createPacket (int destination, byte[] data) {
        Queue<Byte> packet = new LinkedList<Byte>();
        // length of packet is the length of the data + destination byte hence + 4.
        int packetLength = data.length + 4;
        byte[] packetLengthBytes = intToBytes(packetLength);
        for (int i = 0; i < packetLengthBytes.length; i++) {
            assert (packetLengthBytes.length == 4);
            // add the packet length
            packet.add(packetLengthBytes[i]);
        }
        byte[] destinationBytes = intToBytes(destination);
        for (int i = 0; i < destinationBytes.length;i++){
            assert (destinationBytes.length == 4);
            packet.add(destinationBytes[i]);
        }

        for (int i = 0; i < data.length; i++) {
            packet.add(data[i]);
        }

        // convert packet to byte array
        byte[] packetArr = new byte[packet.size()];
        Iterator<Byte> i = packet.iterator();
        int j = 0;
        while (i.hasNext()) {
            packetArr[j++] = i.next();
        }
        return packetArr;
	
	
    } // createPacket ()
    // =========================================================================


    // =========================================================================
    /**
     * Randomly choose the link through which to send a packet given its
     * destination.
     *
     * @param destination The address to which this packet is being sent.
     */
    protected DataLinkLayer route (int destination) {
        logger.entering(RandomNetworkLayer.class.getName(), new Throwable().getStackTrace()[0].getMethodName());
        // follow a random policy to route the link through
        return randomRouting();

	
    } // route ()
    // =========================================================================

    /**
     * @brief Select a random link through which to send a packet.
     * @return a random link
     * @throws NullPointerException if no link is available to choose.
     */
    private DataLinkLayer randomRouting () {
        // Convert the dataLink hashmap to an Array List.
        Collection<DataLinkLayer> dataLinkCollection = dataLinkLayers.values();
        List<DataLinkLayer> dataLinkList = new ArrayList<>(dataLinkCollection);
       // pick a random link.
        Collections.shuffle(dataLinkList);
        DataLinkLayer randomLink = dataLinkList.get(0);
        if (randomLink == null) {
            throw new NullPointerException("No random link");
        }
        logger.info("Choosen random link: " + randomLink); // log information
        return randomLink;
    }

    }
    // =========================================================================
    /**
     * Examine a buffer to see if it's data can be extracted as a packet; if so,
     * do it, and return the packet whole.
     *
     * @param buffer The receive-buffer to be examined.
     * @return the packet extracted packet if a whole one is present in the
     *         buffer; <code>null</code> otherwise.
     */
    protected byte[] extractPacket (Queue<Byte> buffer) {
        Queue<Byte> packetsBuffer = new LinkedList<>(buffer);
        if (buffer.size() < 4){
            return null;
        }
        int packetLength = byteToInteger(packetsBuffer);
        byte[] packet = new byte[packetLength];
        if (packetLength > buffer.size()){
            // buffer is too small to contain the packet
            return null;
        }
        // found a whole packet;
        for (int i = 0; i < packetLength; i++) {
            // fill the packets array wit the whole packet
            packet[i] = buffer.remove();
        }
       return packet;
    } // extractPacket ()
    // =========================================================================

    private int byteToInteger(Queue<Byte> buffer) {
        int intSize = 4; // an int is 4 bytes long.
        int value = 0;

        for (int i = 0; i < intSize; i++) {
            value = (value << 8) + ( buffer.remove() & 0xFF);
        }
    }


    // =========================================================================
    /**
     * Given a received packet, process it.  If the destination for the packet
     * is this host, then deliver its data to the client layer.  If the
     * destination is another host, route and send the packet.
     *
     * @param packet The received packet to process.
     * @see   createPacket
     */
    protected void processPacket (byte[] packet) {

	// COMPLETE ME
	
    } // processPacket ()
    // =========================================================================
    


    // =========================================================================
    // INSTANCE DATA MEMBERS

    /** The random source for selecting routes. */
    private Random random;
    // =========================================================================



    // =========================================================================
    // CLASS DATA MEMBERS

    /** The offset into the header for the length. */
    public static final int     lengthOffset      = 0;

    /** The offset into the header for the source address. */
    public static final int     sourceOffset      = lengthOffset + Integer.BYTES;

    /** The offset into the header for the destination address. */
    public static final int     destinationOffset = sourceOffset + Integer.BYTES;

    /** How many total bytes per header. */
    public static final int     bytesPerHeader    = destinationOffset + Integer.BYTES;

    /** Whether to emit debugging information. */
    public static final boolean debug             = false;
   // =========================================================================


    
// =============================================================================
} // class RandomNetworkLayer
// =============================================================================
