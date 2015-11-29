package views;

import app.BandHeroApp;
import controllers.ProfileController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ProfileView extends BaseView {

    public ProfileView() {
        super(new BorderLayout());
        createComponents();
    }

    public void createComponents() {
        //Set profile image width and height
        profileImgHeight = profileImgWidth = 100;

        //Get Controller Reference
        ProfileController controller = (ProfileController) BandHeroApp.getInstance().getController();

        //Set Margins
        this.setBorder(new EmptyBorder(10,10,10,10));

        //Add User Image
        controller.getUser().getProfileImage().getImage();
        userImage = new JLabel(new ImageIcon(resizeProfileImage(controller.getUser().getProfileImage(), profileImgWidth, profileImgHeight)));
        userImage.setPreferredSize(new Dimension(profileImgWidth, profileImgHeight));
        userImage.setVerticalAlignment(SwingConstants.TOP);

        //Add Nav Menu
        // TODO create nav menus for Musician / Band and add it under the user image

        //Add search bar
        // TODO create search bar component and add it to the top of the BorderLayout CENTER

        //Add "News Feed"
        // TODO create "News Feed" component and add it under search bar.

        //Add Components to Container
        this.add(userImage, BorderLayout.LINE_START);

    }

    private Image resizeProfileImage(ImageIcon imgIcon, int w, int h) {
        Image img = imgIcon.getImage();
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }

    JLabel userImage;
    private int profileImgWidth, profileImgHeight;
}