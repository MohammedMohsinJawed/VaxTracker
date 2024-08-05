import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class VaccineAvailabilityChecker extends JFrame {
    private static final String SOFTWARE_VERSION = "v1.1";
    private static final String IST = "Asia/Kolkata";
    private static final String DEFAULT_PINCODE = "110096";
    private JTextField pincodeTextField;
    private JTextField dateTextField;
    private JTextArea resultBoxAvl, resultBoxCent, resultBoxAge, resultBoxVacc, resultBoxD1, resultBoxD2, resultBoxD1D2;
    private JLabel labelDateNow, labelTimeNow;
    private JCheckBox todayDateChkbox;

    public VaccineAvailabilityChecker() {
        setTitle("Vaccine Availability Checker " + SOFTWARE_VERSION);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 480);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel1 = new JPanel();
        panel1.setBackground(new Color(92, 76, 225));
        panel1.setBounds(0, 0, 180, 120);
        contentPane.add(panel1);
        panel1.setLayout(null);

        labelDateNow = new JLabel("Current Date");
        labelDateNow.setFont(new Font("Verdana", Font.BOLD, 12));
        labelDateNow.setBounds(20, 40, 100, 14);
        panel1.add(labelDateNow);

        labelTimeNow = new JLabel("Current Time");
        labelTimeNow.setFont(new Font("Verdana", Font.PLAIN, 12));
        labelTimeNow.setBounds(20, 60, 100, 14);
        panel1.add(labelTimeNow);

        JPanel panel2 = new JPanel();
        panel2.setBackground(new Color(134, 122, 233));
        panel2.setBounds(180, 0, 520, 120);
        contentPane.add(panel2);
        panel2.setLayout(null);

        JLabel labelPincode = new JLabel("Pincode");
        labelPincode.setFont(new Font("Verdana", Font.PLAIN, 11));
        labelPincode.setBounds(220, 15, 70, 14);
        panel2.add(labelPincode);

        JLabel labelDate = new JLabel("Date");
        labelDate.setFont(new Font("Verdana", Font.PLAIN, 11));
        labelDate.setBounds(380, 15, 46, 14);
        panel2.add(labelDate);

        JLabel labelDateFormat = new JLabel("[dd-mm-yyyy]");
        labelDateFormat.setFont(new Font("Verdana", Font.PLAIN, 7));
        labelDateFormat.setBounds(420, 18, 70, 10);
        panel2.add(labelDateFormat);

        JLabel labelSearchVacc = new JLabel("Search \nAvailable Vaccine");
        labelSearchVacc.setFont(new Font("Verdana", Font.PLAIN, 8));
        labelSearchVacc.setBounds(570, 70, 100, 20);
        panel2.add(labelSearchVacc);

        pincodeTextField = new JTextField(DEFAULT_PINCODE);
        pincodeTextField.setBounds(220, 40, 86, 20);
        panel2.add(pincodeTextField);
        pincodeTextField.setColumns(10);

        dateTextField = new JTextField();
        dateTextField.setBounds(380, 40, 86, 20);
        panel2.add(dateTextField);
        dateTextField.setColumns(10);

        resultBoxAvl = new JTextArea();
        resultBoxAvl.setBounds(3, 152, 40, 200);
        contentPane.add(resultBoxAvl);

        resultBoxCent = new JTextArea();
        resultBoxCent.setBounds(75, 152, 200, 200);
        contentPane.add(resultBoxCent);

        resultBoxAge = new JTextArea();
        resultBoxAge.setBounds(330, 152, 40, 200);
        contentPane.add(resultBoxAge);

        resultBoxVacc = new JTextArea();
        resultBoxVacc.setBounds(400, 152, 60, 200);
        contentPane.add(resultBoxVacc);

        resultBoxD1 = new JTextArea();
        resultBoxD1.setBounds(490, 152, 40, 200);
        contentPane.add(resultBoxD1);

        resultBoxD2 = new JTextArea();
        resultBoxD2.setBounds(555, 152, 40, 200);
        contentPane.add(resultBoxD2);

        resultBoxD1D2 = new JTextArea();
        resultBoxD1D2.setBounds(630, 152, 40, 200);
        contentPane.add(resultBoxD1D2);

        JButton searchButton = new JButton("Search");
        searchButton.setBounds(600, 25, 89, 23);
        panel2.add(searchButton);

        JRadioButton radioLocation = new JRadioButton("Current location");
        radioLocation.setBounds(215, 65, 150, 23);
        panel2.add(radioLocation);

        todayDateChkbox = new JCheckBox("Today");
        todayDateChkbox.setBounds(375, 65, 97, 23);
        panel2.add(todayDateChkbox);

        JCheckBox tomorrowDateChkbox = new JCheckBox("Tomorrow");
        tomorrowDateChkbox.setBounds(435, 65, 97, 23);
        tomorrowDateChkbox.setEnabled(false);
        panel2.add(tomorrowDateChkbox);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchVaccineAvailability();
            }
        });

        todayDateChkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (todayDateChkbox.isSelected()) {
                    dateTextField.setText(getCurrentDate());
                    tomorrowDateChkbox.setEnabled(false);
                } else {
                    tomorrowDateChkbox.setEnabled(true);
                }
            }
        });

        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateClock();
            }
        });
        timer.start();

        updateClock();
    }

    private void updateClock() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss a");
        dateFormat.setTimeZone(TimeZone.getTimeZone(IST));
        timeFormat.setTimeZone(TimeZone.getTimeZone(IST));
        Date now = new Date();
        labelDateNow.setText(dateFormat.format(now));
        labelTimeNow.setText(timeFormat.format(now));
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat.setTimeZone(TimeZone.getTimeZone(IST));
        Date now = new Date();
        return dateFormat.format(now);
    }

    private void searchVaccineAvailability() {
        clearResultBoxes();
        String pincode = pincodeTextField.getText().trim();
        String date = dateTextField.getText();

        try {
            String url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode=" + pincode + "&date=" + date;
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Chrome/84.0.4147.105 Safari/537.36");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray sessions = jsonResponse.getJSONArray("sessions");

                if (sessions.length() == 0) {
                    JOptionPane.showMessageDialog(this, "Vaccine not yet arrived for the given date");
                } else {
                    for (int i = 0; i < sessions.length(); i++) {
                        JSONObject session = sessions.getJSONObject(i);
                        int ageLimit = session.getInt("min_age_limit");
                        String centerName = session.getString("name");
                        String vaccineName = session.getString("vaccine");
                        int availableCapacity = session.getInt("available_capacity");
                        int qntyDose1 = session.getInt("available_capacity_dose1");
                        int qntyDose2 = session.getInt("available_capacity_dose2");

                        String currStatus = availableCapacity > 0 ? "Available" : "NA";
                        String ageGroup = ageLimit == 45 ? "45+" : "18-44";

                        resultBoxAvl.append(currStatus + "\n");
                        resultBoxCent.append(centerName + "\n");
                        resultBoxAge.append(ageGroup + "\n");
                        resultBoxVacc.append(vaccineName + "\n");
                        resultBoxD1.append(qntyDose1 + "\n");
                        resultBoxD2.append(qntyDose2 + "\n");
                        resultBoxD1D2.append(availableCapacity + "\n");
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "GET request not worked");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No Available center(s) for the given Pincode and date");
            e.printStackTrace();
        }
    }

    private void clearResultBoxes() {
        resultBoxAvl.setText("");
        resultBoxCent.setText("");
        resultBoxAge.setText("");
        resultBoxVacc.setText("");
        resultBoxD1.setText("");
        resultBoxD2.setText("");
        resultBoxD1D2.setText("");
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    VaccineAvailabilityChecker frame = new VaccineAvailabilityChecker();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
