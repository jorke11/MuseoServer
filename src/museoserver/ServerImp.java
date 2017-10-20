/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package museoserver;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import museocommon.DataClient;
import museocommon.DataUser;
import museocommon.MuseoInt;

/**
 *
 * @author pinedo
 */
public class ServerImp extends UnicastRemoteObject implements MuseoInt {

    List<DataUser> currentUser = new ArrayList<>();
    List<DataClient> clients = new ArrayList<>();

    public ServerImp(List<DataUser> users) throws RemoteException {
        super();
        currentUser = users;
    }

    public void printUsers(List<DataUser> user) {
        String users = "";
        System.out.println("=== Impresion desde Imple");
        for (int i = 0; i < user.size(); i++) {
            System.out.println(user.get(i).getId() + " " + user.get(i).getRole_id() + " " + user.get(i).getUser() + " " + user.get(i).getPassword());
        }
    }

    @Override
    public String decirHola(String nombre) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<DataUser> auth(String user, String password) throws RemoteException {
        List<DataUser> response = null;

        for (int i = 0; i < currentUser.size(); i++) {
            if (currentUser.get(i).getUser().equals(user) && currentUser.get(i).getPassword().equals(password)) {
                response = new ArrayList<>();
                response.add(new DataUser(currentUser.get(i).getId(), currentUser.get(i).getRole_id(),
                        currentUser.get(i).getUser(), currentUser.get(i).getPassword()));
                break;
            }
        }

        return response;
    }

    @Override
    public List<DataClient> add(int user_id, String name, int age) throws RemoteException {
        List<DataClient> response = new ArrayList<>();
        System.out.println("intentado agregar");
        int id = clients.size();
        int valor = (age < 18) ? 5000 : 20000;
        Date date = new Date();
        String finput = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        clients.add(new DataClient(id, user_id, name, age, valor, "new", finput));
        response.add(new DataClient(id, user_id, name, age, valor, "new", finput));
        System.out.println("data " + response.toString());
        return response;
    }

    @Override
    public boolean modify(int index, String status) throws RemoteException {
        boolean res = true;
        clients.get(index).setStatus("old");
        Date date = new Date();
        String foutput = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
        clients.get(index).setdateOutput(foutput);
        return res;
    }

    @Override
    public Map<String, Integer> getReport() throws RemoteException {
        System.out.println("ingreso a getReport");
        Map<String, Integer> reports = new HashMap<String, Integer>();

        int quantityPerson = 0, total = 0, adult = 0, child = 0, totalAdult = 0, totalChild = 0;

        for (DataClient row : clients) {
            if (row.getStatus().equals("new")) {
                quantityPerson++;
            }

            if (row.getAge() < 18) {
                child++;
                totalChild += row.getValor();
            } else {
                adult++;
                totalAdult += row.getValor();
            }

            total += row.getValor();
        }

        reports.put("quantity_total", clients.size());
        reports.put("total_new", quantityPerson);
        reports.put("total", total);
        reports.put("totalchild", totalChild);
        reports.put("totaladult", totalAdult);

        reports.put("quantitychild", child);
        reports.put("quantityadult", adult);

        return reports;
    }

    @Override
    public List<DataClient> getAllClient() throws RemoteException {
        return clients;
    }

}
