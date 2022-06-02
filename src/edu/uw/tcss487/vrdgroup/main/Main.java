package edu.uw.tcss487.vrdgroup.main;

import edu.uw.tcss487.vrdgroup.main.features.EllipticCrypto;
import edu.uw.tcss487.vrdgroup.main.features.Hash;
import edu.uw.tcss487.vrdgroup.main.features.Signature;
import edu.uw.tcss487.vrdgroup.main.features.SymmetricCrypto;

import java.util.Scanner;

/**
 * @author Vu Nguyen
 * Main application
 */
public class Main {

    private static Scanner sc = new Scanner(System.in);

    /**
     * Main program
     * @param args
     */
    public static void main(String[] args) {
        menu();
    }



    /**
     * Display menus
     */
    public static void menu() {
        int select = 0;
        do {
            System.out.println("Type a number to choose the function you want to use:");
            System.out.println("\nPart 1: HA-3 derived function KMACXOF256\n");
            System.out.println("1. Compute a plain cryptographic hash of a given file.");
            System.out.println("2. Compute a plain cryptographic hash of text input.");
            System.out.println("3. Encrypt a given data file symmetrically under a given passphrase.");
            System.out.println("4. Decrypt a given symmetric cryptogram under a given passphrase.");
            System.out.println("5. Compute an authentication tag (MAC) of a given file under a given passphrase.");
            System.out.println("\nPart 2: ECDHIES encryption and Schnorr signatures\n");
            System.out.println("6. Generate an elliptic key pair from a given passphrase and write the public key to a file.");
            System.out.println("   Encrypt the private key under the given password and write it to a file as well.");
            System.out.println("7. Encrypt a data file under a given elliptic public key file.");
            System.out.println("8. Decrypt a given elliptic-encrypted file from a given password.");
            System.out.println("9. Encrypt/(not decrypt) text input by the user directly to the app instead of having to be read from a file.");
            System.out.println("10. Sign a given file from a given password and write the signature to a file.");
            System.out.println("11. Verify a given data file and its signature file under a given public key file.");
            System.out.println("12. Encrypting a file under the recipient’s public key and also signing it under the user’s own private key.");

            select = sc.nextInt();
            boolean cont = false;
            do {
                switch (select) {
                    case 1:
                        cont = computeHash(true);
                        break;
                    case 2:
                        cont = computeHash(false);
                        break;
                    case 3:
                        cont = encryptSymmetrical();
                        break;
                    case 4:
                        cont = decryptSymmetrical();
                        break;
                    case 5:
                        cont = computeAuthenticateTag();
                        break;
                    case 6:
                        cont = generateEllipticKeys();
                        break;
                    case 7:
                        cont = encryptElliptic();
                        break;
                    case 8:
                        cont = decryptElliptic();
                        break;
                    case 9:
                        cont = encryptTextElliptic();
                        break;
                    case 10:
                        cont = generateSignature();
                        break;
                    case 11:
                        cont = vefifySig();
                        break;
                    default:
                        cont = false;
                }
            } while (cont);
        } while (select != 0);
    }

    /**
     * Compute hash from a file
     * @return
     */
    public static boolean computeHash(boolean isFromFile) {
        byte[] hash = null;

        if (isFromFile) {
            System.out.print("Please enter the file path to compute hash (data.txt): ");
            String filePath = sc.next();
            if (filePath.isEmpty()) {
                filePath = "data.txt";
            }
            hash = Hash.computeHash(filePath);
        } else {
            System.out.print("Please enter your text content, then push enter button twice times when you finish: ");
            String input = Utils.getTextInput();
            hash = Hash.computeHash(input.getBytes());
        }

        if (hash != null) {
            Utils.printBits(Utils.byteArrayToBitArray(hash), "The plain cryptographic hash is:");
        } else {
            System.out.println("Something wrong, I cannot read the content. Please try again.");
        }
        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Encrypt symmetrically a file under passphrase and save the symmetric cryptogram (z, c, t) into 3 files (smg_z, smg_c, smg_t)
     * @return
     */
    public static boolean encryptSymmetrical() {
        System.out.print("Please enter the file path to encrypt (data.txt): ");
        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "data.txt";
        }
        System.out.print("Please enter your passphrase: ");
        String pw = sc.next();

        Utils.SymmetricCryptogram smg = SymmetricCrypto.encryptSymmetrically(Utils.readFile(filePath), pw.getBytes());
        Utils.printBits(Utils.byteArrayToBitArray(smg.z), "z:");
        Utils.printBits(Utils.byteArrayToBitArray(smg.c), "c:");
        Utils.printBits(Utils.byteArrayToBitArray(smg.t), "t:");

        Utils.writeObject("smg", smg);
        System.out.println("I saved the symmetric cryptogram: (z, c, t) into file named smg");

        System.out.print("Do you want to decrypt? y/n ");
        if ("y".equals(sc.next().toLowerCase())) {
            System.out.print("Please enter your passphrase: ");
            pw = sc.next();
            byte[] m = SymmetricCrypto.decryptSymmetrically(smg, pw.getBytes());
            if (m == null) {
                System.out.println("I cannot decrypt, you may input wrong passphrase.");
            } else {
                System.out.println("Your message is: ");
                System.out.println(new String(m));
            }
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Decrypt symmetric cryptogram (z, c, t) from files
     * @return
     */
    public static boolean decryptSymmetrical() {
        System.out.print("Please enter the file path of symmetric cryptogram (z, c, t) to encrypt (smg): ");

        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "smg";
        }

        Utils.SymmetricCryptogram smg = (Utils.SymmetricCryptogram) Utils.readObject(filePath);

        System.out.print("Please enter your passphrase: ");
        String pw = sc.next();

        byte[] m = SymmetricCrypto.decryptSymmetrically(smg, pw.getBytes());
        if (m == null) {
            System.out.println("I cannot decrypt, you may input wrong passphrase.");
        } else {
            System.out.println("Your message is: ");
            System.out.println(new String(m));
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Compute authenticate tag
     * @return
     */
    public static boolean computeAuthenticateTag() {
        byte[] tag = null;

        System.out.print("Please enter the file path to compute tag (data.txt): ");
        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "data.txt";
        }

        System.out.print("Please enter your passphrase: ");
        String pw = sc.next();

        tag = Hash.computeAuthenticateTag(Utils.readFile(filePath), pw.getBytes());

        if (tag != null) {
            Utils.printBits(Utils.byteArrayToBitArray(tag), "The tag is:");
            Utils.writeFile("tag", tag);
            System.out.println("I saved the tag into a file named: tag");
        } else {
            System.out.println("Something wrong, I cannot read the content. Please try again.");
        }
        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Generate Elliptic key pair
     */
    public static boolean generateEllipticKeys() {
        System.out.print("Please enter your passphrase to generate elliptic key pair: ");
        String pw = sc.next();

        Utils.EllipticKeyPair keyPair = EllipticCrypto.generateEllipticKeys(pw.getBytes());
        System.out.println("The key pair is:");
        System.out.println("The public key s:");
        System.out.println(keyPair.s.toString());
        System.out.println("The private key V:");
        System.out.println("x = " + keyPair.V.x.toString());
        System.out.println("y = " + keyPair.V.y.toString());

        Utils.writeObject("V", keyPair.V);
        System.out.println("I saved the public key into a file named: V");
        Utils.SymmetricCryptogram cryptogram = SymmetricCrypto.encryptSymmetrically(keyPair.s.toByteArray(), pw.getBytes());
        Utils.writeObject("s", cryptogram);
        System.out.println("I saved the encrypted private key into a file named: s");

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Encrypt symmetrically a file under passphrase using elliptic public key
     * and save the symmetric cryptogram (Z, c, t) into file
     * @return
     */
    public static boolean encryptElliptic() {
        System.out.print("Please enter the file path to encrypt (data.txt): ");
        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "data.txt";
        }
        System.out.print("Please enter the the file path of the public key: ");
        String publicKeyFilePath = sc.next();

        EdwardsPoint V = (EdwardsPoint) Utils.readObject(publicKeyFilePath);

        Utils.EllipticCryptogram elg = EllipticCrypto.ellipticEncrypt(Utils.readFile(filePath), V);
        System.out.println("-----------------------------");
        System.out.println("Z:");
        System.out.println("-----------------------------");
        System.out.println("x = " + elg.Z.x.toString());
        System.out.println("y = " + elg.Z.y.toString());
        Utils.printBits(Utils.byteArrayToBitArray(elg.c), "c:");
        Utils.printBits(Utils.byteArrayToBitArray(elg.t), "t:");

        Utils.writeObject("elg", elg);
        System.out.println("I saved the symmetric cryptogram: (Z, c, t) into file named elg");

        System.out.print("Do you want to decrypt? y/n ");
        if ("y".equals(sc.next().toLowerCase())) {
            System.out.print("Please enter your passphrase: ");
            String pw = sc.next();
            byte[] m = EllipticCrypto.ellipticDecrypt(elg, pw.getBytes());
            if (m == null) {
                System.out.println("I cannot decrypt, you may input wrong passphrase.");
            } else {
                System.out.println("Your message is: ");
                System.out.println(new String(m));
            }
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Encrypt symmetrically an input text under passphrase using elliptic public key
     * and save the symmetric cryptogram (Z, c, t) into file
     * @return
     */
    public static boolean encryptTextElliptic() {
        System.out.print("Please enter your text content, then push enter button twice times when you finish: ");
        String input = Utils.getTextInput();

        System.out.print("Please enter the the file path of the public key: ");
        String publicKeyFilePath = sc.next();

        EdwardsPoint V = (EdwardsPoint) Utils.readObject(publicKeyFilePath);

        Utils.EllipticCryptogram elg = EllipticCrypto.ellipticEncrypt(input.getBytes(), V);
        System.out.println("-----------------------------");
        System.out.println("Z:");
        System.out.println("-----------------------------");
        System.out.println("x = " + elg.Z.x.toString());
        System.out.println("y = " + elg.Z.y.toString());
        Utils.printBits(Utils.byteArrayToBitArray(elg.c), "c:");
        Utils.printBits(Utils.byteArrayToBitArray(elg.t), "t:");

        Utils.writeObject("elg", elg);
        System.out.println("I saved the symmetric cryptogram: (Z, c, t) into file named elg");

        System.out.print("Do you want to decrypt? y/n ");
        if ("y".equals(sc.next().toLowerCase())) {
            System.out.print("Please enter your passphrase: ");
            String pw = sc.next();
            byte[] m = EllipticCrypto.ellipticDecrypt(elg, pw.getBytes());
            if (m == null) {
                System.out.println("I cannot decrypt, you may input wrong passphrase.");
            } else {
                System.out.println("Your message is: ");
                System.out.println(new String(m));
            }
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Decrypt elliptic cryptogram (Z, c, t) from files
     * @return
     */
    public static boolean decryptElliptic() {
        System.out.print("Please enter the file path of elliptic cryptogram (Z, c, t) to encrypt (elg): ");

        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "elg";
        }

        Utils.EllipticCryptogram elg = (Utils.EllipticCryptogram) Utils.readObject(filePath);

        System.out.print("Please enter your passphrase: ");
        String pw = sc.next();

        byte[] m = EllipticCrypto.ellipticDecrypt(elg, pw.getBytes());
        if (m == null) {
            System.out.println("I cannot decrypt, you may input wrong passphrase.");
        } else {
            System.out.println("Your message is: ");
            System.out.println(new String(m));
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Compute authenticate tag
     * @return
     */
    public static boolean generateSignature() {
        Utils.Signature sig = null;

        System.out.print("Please enter the file path to generate signature (data.txt): ");
        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "data.txt";
        }

        System.out.print("Please enter your passphrase: ");
        String pw = sc.next();

        sig = Signature.sign(Utils.readFile(filePath), pw.getBytes());

        if (sig != null) {
            System.out.println("-----------------------------");
            System.out.println("Signature:");
            System.out.println("-----------------------------");
            System.out.println("h = " + sig.h.toString());
            System.out.println("z = " + sig.z.toString());
            Utils.writeObject("sig", sig);
            System.out.println("I saved the signature into a file named: sig");
        } else {
            System.out.println("Something wrong, I cannot read the content. Please try again.");
        }
        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }

    /**
     * Verify signature and data file using a public key
     * @return
     */
    public static boolean vefifySig() {
        System.out.print("Please enter the file path to verify (data.txt): ");
        String filePath = sc.next();
        if (filePath.isEmpty()) {
            filePath = "data.txt";
        }
        System.out.print("Please enter the the file path of signature: ");
        String sigFilePath = sc.next();

        System.out.print("Please enter the the file path of the public key: ");
        String publicKeyFilePath = sc.next();

        byte[] m = Utils.readFile(filePath);
        Utils.Signature sig = (Utils.Signature )Utils.readObject(sigFilePath);
        EdwardsPoint V = (EdwardsPoint) Utils.readObject(publicKeyFilePath);

        boolean verified = Signature.verifySignature(sig, m, V);
        if (verified) {
            System.out.println("The file and its signature match.");
        } else {
            System.out.println("The file and its signature don't match.");
        }

        System.out.print("Do you want to continue? y/n ");
        return "y".equals(sc.next().toLowerCase());
    }
}
