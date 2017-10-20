/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museoserver;

import museocommon.DataUser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author pinedo
 */
public class MuseoServer {

    static List<DataUser> newUser = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ParseException {
        newUser = createUserMain();
//        Date date = new Date();
//        String str = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
//        System.out.println(str);

        InputStreamReader ent = new InputStreamReader(System.in);
        BufferedReader buf = new BufferedReader(ent);
        String numPuerto, URLRegistro;
        try {
//            System.out.println("Introducir el número de puerto del registro RMI:");
//            numPuerto = buf.readLine().trim();
            numPuerto = "8881";
//            int numPuertoRMI = Integer.parseInt(numPuerto);
            int numPuertoRMI = Integer.parseInt(numPuerto);
            arrancarRegistro(numPuertoRMI);
            ServerImp objExportado = new ServerImp(newUser);
            URLRegistro = "rmi://localhost:" + numPuerto + "/holaMundo";
            Naming.rebind(URLRegistro, objExportado);
            System.out.println("Servidor registrado. El registro contiene actualmente:");
            // lista de los nombres que se encuentran en el registro actualmente
            listaRegistro(URLRegistro);
            System.out.println("Servidor HolaMundo preparado.");
        } // fin try
        catch (Exception excr) {
            System.out.println("Excepción en HolaMundoServidor.main: " + excr);
        } // fin catch 
    }

    private static void arrancarRegistro(int numPuertoRMI) throws RemoteException {
        try {
            Registry registro = LocateRegistry.getRegistry(numPuertoRMI);
            registro.list();
        } catch (RemoteException e) {
            // Registro no válido en este puerto System.out.println
            System.out.println("El registro RMI no se puede localizar en el puerto " + numPuertoRMI);
            Registry registro = LocateRegistry.createRegistry(numPuertoRMI);
            System.out.println("Registro RMI creado en el puerto " + numPuertoRMI);
        } // fin catch
    } // fin arrancarRegistro

    private static void listaRegistro(String URLRegistro) throws RemoteException, MalformedURLException {
        System.out.println("Registro " + URLRegistro + "contiene: ");
        String[] nombres = Naming.list(URLRegistro);
        for (int i = 0; i < nombres.length; i++) {
            System.out.println(nombres[i]);
        }
    } // fin listaRegistro

    private static List<DataUser> createUserMain() {
        List<DataUser> newUser = new ArrayList<>();
        newUser.add(new DataUser(1, 1, "admin", "admin"));
        newUser.add(new DataUser(2, 2, "jorge", "pinedo"));
        return newUser;
    } // fin listaRegistro

}
