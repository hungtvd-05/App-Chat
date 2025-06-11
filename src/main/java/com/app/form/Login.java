/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.app.form;

import com.app.component.PanelCover;
import com.app.component.PanelLoginAndRegister;
import com.app.component.PanelMessage;
import com.app.component.PanelVerifyMail;
import com.app.event.EventLogin;
import com.app.event.EventMessage;
import com.app.event.PublicEvent;
import com.app.main.Main;
import com.app.model.Model_Login;
import com.app.model.Model_Message;
import com.app.model.Model_Register;
import com.app.model.UserAccount;
import com.app.service.Service;
import com.formdev.flatlaf.util.Animator;
import io.socket.client.Ack;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.TimingTargetAdapter;


/**
 *
 * @author LENOVO
 */
public class Login extends javax.swing.JLayeredPane {

    /**
     * Creates new form Login
     */
    
    private MigLayout layout;
    private PanelCover cover;
    private PanelLoginAndRegister panelLAR;
    private PanelVerifyMail panelVerifyMail;
    private final double addSize = 30;
    private final double coverSize = 40;
    private double loginSize = 60;
    private boolean isLogin = true;
    private final DecimalFormat df = new DecimalFormat("##0.###");
    
    @Getter
    private static Login instance = null;
    
    
    public Login() {
        instance = this;
        initComponents();
        init();
    }
    
    private void init() {
        
        PublicEvent.getInstance().setEventLogin(new EventLogin() {
            @Override
            public void login(Model_Login data) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        PublicEvent.getInstance().getEventMain().showLoading(true);
                        
                        System.out.println(data.toJsonObject().toString());

                        Service.getInstance().getClient().emit("login", data.toJsonObject(), new Ack() {
                            @Override
                            public void call(Object... os) {
                                if (os.length > 0) {
                                    boolean action = (boolean)os[0];
                                    if (action) {
                                        Service.getInstance().setUserAccount(new UserAccount(os[1]));
                                        PublicEvent.getInstance().getEventMain().initChat();
                                        PublicEvent.getInstance().getEventMain().showLoading(false);
                                        Main.getInstance().setTitle(Service.getInstance().getUserAccount().getUserName());
                                        setVisible(false);
                                    } else {
                                        PublicEvent.getInstance().getEventMain().showLoading(false);
                                        Login.getInstance().showMessage(PanelMessage.MessageType.ERROR, "Sai mat khau");
                                    }
                                } else {
                                    PublicEvent.getInstance().getEventMain().showLoading(false);
                                }
                            }
                        });
                        
                        
                    }
                }).start();
            }

            @Override
            public void register(Model_Register data, EventMessage message) {
                Service.getInstance().getClient().emit("register", data.toJSONObject(), new Ack() {
                    @Override
                    public void call(Object... os) {
                        if (os.length > 0) {
                            Model_Message ms = new Model_Message((boolean)os[0], os.length > 1 && os[1] != null ? os[1].toString() : "");
                            
                            
                            
                            if (ms.isAction()) {
                                UserAccount userAccount = new UserAccount((Object)os[2]);
                                Service.getInstance().setUserAccount(userAccount);
                                Main.getInstance().setTitle(userAccount.getUserName());
                            }
                            
                            message.callMessage(ms);
                            
                            
                        }
                        System.out.println("Hello 5");
                        
                    }
                });
            }

            @Override
            public void goRegister() {
            }

            @Override
            public void goLogin() {
            }
        });
        
        
        
        
        ActionListener eventRegister = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Đăng ký
            }
        };
        
        ActionListener eventLogin = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                Đăng nhập
            }
        };
        
        
                
        
        
        layout = new MigLayout("fill, insets 0");
        cover = new PanelCover();
        panelLAR = new PanelLoginAndRegister(eventRegister, eventLogin);
        
        panelVerifyMail = new PanelVerifyMail(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        },
        new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage(PanelMessage.MessageType.ERROR, "Fail");
                panelVerifyMail.setVisible(false);
            }
        });
        
        
        Animator.TimingTarget target = new Animator.TimingTarget() {
            @Override
            public void timingEvent(float f) {
                
                double fractionCover;
                double fractionLogin;
                double size = coverSize;
                
                if (f<=0.5f) {
                    size += f * addSize;
                } else {
                    size += addSize - f * addSize;
                }
                
                if (!isLogin) {
                    fractionCover = 1f - f; 
                    fractionLogin = f;
                } else {
                    fractionCover = f;
                    fractionLogin = 1f - f;
                }
                
                
                if (f>=0.5) {
                    panelLAR.showLogin(isLogin);
                }
                
                
                fractionCover = Double.valueOf(df.format(fractionCover));
                fractionLogin = Double.valueOf(df.format(fractionLogin));
                layout.setComponentConstraints(panelLAR, "width "+loginSize+"%, pos "+fractionLogin+"al 0 n 100%");
                layout.setComponentConstraints(cover, "width "+size+"%, pos "+fractionCover+"al 0 n 100%");
                revalidate();
            }

            @Override
            public void begin() {
                Main.getInstance().setEnabled(false);
            }

            @Override
            public void end() {
                isLogin = !isLogin;
                Main.getInstance().setEnabled(true);
                
            }

        };
        
        Animator animator = new Animator(1000, target);
        
        panelLAR.setEvent(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!animator.isRunning()) {
                    animator.start();
                }
            }
        });
        
        this.setLayout(layout);
        this.setLayer(panelVerifyMail, JLayeredPane.POPUP_LAYER);
        this.add(panelVerifyMail, "pos 0 0 100% 100%");
        this.add(cover, "width "+coverSize+"%, pos 0al 0 n 100%");
        this.add(panelLAR, "width "+loginSize+"%, pos 1al 0 n 100%");
               
        cover.addEvent1(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                panelVerifyMail.setVisible(true);
            }
        });
        
        cover.addEvent2(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showMessage(PanelMessage.MessageType.SUCCESS, "Hello");
            }
        });
        
        
    }
    
    public void showMessage(PanelMessage.MessageType messageType, String message) {
        PanelMessage panelMessage = new PanelMessage();
        panelMessage.setSizeMess(message.length()*7+30,30);
        panelMessage.showMessage(messageType, message);
        Animator.TimingTarget target = new Animator.TimingTarget(){
            public void begin() {
                if(!panelMessage.isShow()) {
                    setLayer(panelMessage, JLayeredPane.POPUP_LAYER+1);
                    add(panelMessage,"pos 0.5al -30",0);
                    panelMessage.setVisible(true);
                    repaint();      
                }
            }

            
            public void end() {
                if (panelMessage.isShow()) {
                    remove(panelMessage);
                    panelMessage.setVisible(false);
                    repaint();
                    revalidate();
                } else {
                    panelMessage.setShow(true);      
                }
            }

            public void timingEvent(float fraction) {
                float f;
                if (panelMessage.isShow()) {
                    f = 40*(1f-fraction);
                } else {
                    f = 40*fraction;
                }
                layout.setComponentConstraints(panelMessage, "pos 0.5al " + (int)(f-30));
                repaint();
                revalidate();    
            }
            
        };
        
        Animator animator = new Animator(300, target);
        animator.start();

        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                    animator.start();
                } catch (Exception e) {
                    System.err.print(e);
                }
            }
        }).start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
