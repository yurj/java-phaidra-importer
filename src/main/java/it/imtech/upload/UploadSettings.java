/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.imtech.upload;

import at.ac.univie.phaidra.api.Phaidra;
import it.imtech.bookimporter.BookImporter;
import it.imtech.metadata.MetaUtility;
import it.imtech.utility.Utility;
import it.imtech.xmltree.XMLTree;
import it.imtech.globals.Globals;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.TitledBorder;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.swingx.JXDatePicker;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author mauro
 */
public class UploadSettings extends javax.swing.JFrame {

    private String username = null;
    private String password = null;
    private String lockObjectsUntil = null;
    private String ApplicationLanguage = null;
    private static UploadSettings istance;
    //https://phaidratest.cab.unipd.it/detail_object/o:16121?tab=0#mda

    public static UploadSettings getInstance(Locale currentLocale) {
        if (istance == null) {
            istance = new UploadSettings();
            istance.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                istance.setVisible(false);
                istance.password = "";
                istance.jTextField3.setText("");
                BookImporter.getInstance().setVisible(true);
            }
        });
        }

        istance.updateLanguageLabel(currentLocale);

        istance.setDefaultValues(currentLocale);

        return istance;
    }

    /**
     * Creates new form UploadSettings
     */
    public UploadSettings() {
        ResourceBundle bundle = ResourceBundle.getBundle(BookImporter.RESOURCES, BookImporter.currentLocale, Globals.loader);

        initComponents();

        JLabel label = new JLabel();
        label.setText(Utility.getBundleString("blockobjupload",bundle));
        label.setPreferredSize(new Dimension(100, 20));

        JXDatePicker datePicker = new JXDatePicker();
        datePicker.setName("blockDataObject");

        jPanel3.setLayout(new MigLayout());
        jPanel3.add(label);
        jPanel3.add(datePicker, "wrap");
        jPanel3.revalidate();

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (dim.width - getSize().width) / 2;
        int y = (dim.height - getSize().height) / 2;
        setLocation(x, y);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    private void setDefaultValues(Locale currentLocale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BookImporter.RESOURCES, BookImporter.currentLocale, Globals.loader);
        jPasswordField1.setText(this.password);
        jTextField3.setText(this.username);


    }

    private void updateLanguageLabel(Locale currentLocale) {
        ResourceBundle bundle = ResourceBundle.getBundle(BookImporter.RESOURCES, BookImporter.currentLocale, Globals.loader);
        jButton1.setText(Utility.getBundleString("startupload",bundle));
        jButton2.setText(Utility.getBundleString("stopupload",bundle));
        jLabel1.setText(Utility.getBundleString("iduteupload",bundle));
        jLabel2.setText(Utility.getBundleString("idpwdupload",bundle));
        this.setTitle(Utility.getBundleString("titlesettings",bundle));
        jPanel2.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), Utility.getBundleString("paneltitleupload",bundle), TitledBorder.LEFT, TitledBorder.TOP));
    }

    private String checkOCRFiles() {
        ResourceBundle bundle = ResourceBundle.getBundle(BookImporter.RESOURCES, BookImporter.currentLocale, Globals.loader);
        String result = "";
        String xmlPath = "";

        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(getClass().getResource("config/ocr.xsd"));
            Validator validator = schema.newValidator();

            XMLTree.savedXmlDoc.getDocumentElement().normalize();

            NodeList nodeLst = XMLTree.savedXmlDoc.getElementsByTagName("book:structure");

            //Scorri bookstructure per trovare coppie(Nome Capitolo, Nome File pagina)
            for (int s = 0; s < nodeLst.getLength(); s++) {
                if (nodeLst.item(s).getNodeType() == Node.ELEMENT_NODE) {
                    Element chapter = (Element) nodeLst.item(s);

                    //Per ogni pagina del capitolo esegui l'upload
                    NodeList leaves = chapter.getChildNodes();
                    for (int z = 0; z < leaves.getLength(); z++) {
                        if (leaves.item(z).getNodeType() == Node.ELEMENT_NODE) {
                            Element page = (Element) leaves.item(z);
                            xmlPath = BookImporter.selectedFolderSep + Utility.changeFileExt(page.getAttribute("pid"));

                            if (new File(xmlPath).isFile()) {
                                try {
                                    validator.validate(new StreamSource(getClass().getResourceAsStream(xmlPath)));
                                } catch (SAXException e) {
                                    result = Utility.getBundleString("error15",bundle) + " " + xmlPath + ": " + e.toString();
                                }
                            } else {
                                result = Utility.getBundleString("error12",bundle) + " " + xmlPath + " " + Utility.getBundleString("error14",bundle);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            result = "Check OCR: " + ex.getMessage();
        }

        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Upload");
        setName("uplSettings");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Impostazioni"));

        jLabel1.setText("ID-utente");

        jLabel2.setText("Password");

        jPasswordField1.setText("jPasswordField1");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 310, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 61, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 86, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField3)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 206, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 42, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Inizia");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Interrompi");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(48, 48, 48)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.setVisible(false);
        this.password = "";
        jTextField3.setText("");
        BookImporter.getInstance().setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ResourceBundle bundle = ResourceBundle.getBundle(BookImporter.RESOURCES, BookImporter.currentLocale, Globals.loader);

        boolean dateOk = false;
        this.username = jTextField3.getText();
        char[] passw = jPasswordField1.getPassword();
        this.password = "";
        for (int i = 0; i < passw.length; i++) {
            this.password += passw[i];
        }
        jPasswordField1.setText("");

        Component[] components = jPanel3.getComponents();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JXDatePicker) {
                JXDatePicker datePicker = (JXDatePicker) components[i];
                SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");

                if (datePicker.getDate()!=null) {
                    try {
                        Date date1 = sdff.parse(sdff.format(datePicker.getDate()));
                        Date date2 = sdff.parse(sdff.format(new Date()));

                        if (date1.compareTo(date2) >= 0) {
                            dateOk = true;
                            this.lockObjectsUntil = sdf.format(datePicker.getDate());
                        } else {
                            JOptionPane.showMessageDialog(this, Utility.getBundleString("datemaggiore",bundle));
                        }
                    } catch (ParseException ex) {
                        Logger.getLogger(UploadSettings.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                else
                    dateOk = true;
            }
        }

        if (dateOk) {
            this.setVisible(false);
            BookImporter.getInstance().setVisible(true);

            String error = "";
            try {
                PhaidraUtils phra = PhaidraUtils.getInstance(null);

                //Wenn es OCR Daten gibt, checken ob fuer jede Seite vorhanden UND ob valid
                if (error.length() < 1 && BookImporter.getInstance().ocrBoxIsChecked()) {
                    error = checkOCRFiles();
                }

                //Metadaten prüfen
                if (error.length() < 1) {
                    BookImporter.getInstance().createComponentMap();
                    error = MetaUtility.getInstance().check_and_save_metadata(BookImporter.selectedFolderSep + Globals.IMP_EXP_METADATA,false);
                }

                if (error.length() < 1) {
                    XMLTree.exportBookstructure(BookImporter.selectedFolderSep);
                }

                if (error.length() < 1) {
                    if (username.length() > 0 && password.length() > 0) {
                        Phaidra phaidra = new Phaidra(phra.getBaseUrl(), phra.getStaticBaseURL(), phra.getStyleSheetURL(), phra.getOaiIdentifier(), username, password);

                        String pdfPath = BookImporter.getInstance().getPathPdf();

                        if (pdfPath.length() > 0 || !BookImporter.getInstance().isBook()) {
                            String text = Utility.getBundleString("info1",bundle);
                            text += (BookImporter.TYPEBOOK == Globals.BOOK) ? Utility.getBundleString("info5",bundle) : Utility.getBundleString("info6",bundle);
                            text += " " + Utility.getBundleString("info7",bundle);

                            Object[] options = {Utility.getBundleString("voc1",bundle), Utility.getBundleString("voc2",bundle)};
                            int n = JOptionPane.showOptionDialog(this,
                                    text + " " + Utility.getBundleString(phra.getserverName(),bundle) + " "
                                    + Utility.getBundleString("info2",bundle) + "?", Utility.getBundleString("info3",bundle),
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

                            if (n == JOptionPane.YES_OPTION) {
                                this.setVisible(false);
                                UploadProgress.createAndShowGUI(phaidra, lockObjectsUntil, pdfPath, username);
                                this.dispose();
                            }
                        } else {
                            JOptionPane.showMessageDialog(BookImporter.getInstance(), Utility.getBundleString("error16",bundle));
                        }
                    } else {
                        JOptionPane.showMessageDialog(BookImporter.getInstance(), Utility.getBundleString("error17",bundle));
                    }
                } else {
                    JOptionPane.showMessageDialog(BookImporter.getInstance(), error.toString());
                }

            } catch (Exception ex) {
                Globals.logger.error(ex.getMessage());
                JOptionPane.showMessageDialog(BookImporter.getInstance(), "Exception: " + ex.getMessage());
            }
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /*
         * Set the Nimbus look and feel
         */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /*
         * If Nimbus (introduced in Java SE 6) is not available, stay with the
         * default look and feel. For details see
         * http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UploadSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UploadSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UploadSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UploadSettings.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /*
         * Create and display the form
         */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                // new UploadSettings().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables
}
