package controller;

import business.*;
import dataaccess.*;
import utils.ImageUtil;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import murach.util.PasswordUtil;
import utils.MailUtilYahoo;

@WebServlet(name = "membershipServlet", urlPatterns = {"/membership"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2,
        maxFileSize = 1024 * 1024 * 10,
        maxRequestSize = 1024 * 1024 * 50)

public class membershipServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        String url = "/login.jsp";

        if (action == null) {
            action = "login";

        } else if (action.equals("signout")) {
            HttpSession session = request.getSession(false);

            User user = (User) session.getAttribute("user");

            user = UserDB.select(user.getUsername());
            String loginTime = getCurrentTime();
            user.setLastLoginTime(loginTime);

            UserDB.insertLoginTime(loginTime, user.getUsername());
            if (session != null) {
                session.removeAttribute("user");
                session.removeAttribute("users");
                session.removeAttribute("numbTwits");

                session.invalidate();
            }
            deleteCookies(request, response);

        } else if (action.equals("profile")) {
            url = "/signup.jsp";

            ArrayList<String> userInfo = new ArrayList<>();      // collect user's input from signup.jsp
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            userInfo.add(user.getFullName());         // 0
            userInfo.add(user.getUsername());         // 1
            userInfo.add(user.getEmailAddress());            // 2
            userInfo.add(user.getPassword());         // 3
            userInfo.add(user.getPassword());         // confirm password
            userInfo.add(user.getBirthDate());      // 5
            userInfo.add(Integer.toString(user.getQuestionNo())); // 6
            userInfo.add(user.getAnswer());           // 7

            reloadUserInput(request, userInfo);

        } else if (action.equals("tweet")) {
            url = "/home.jsp";

            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            String email = user.getUsername();

            String twitMessage = request.getParameter("textArea");

            String twitTime = getCurrentTime();

            if (twitMessage.equals("") == false) {

                Twit twit = new Twit();
                twit.setTwitMessage(changeMentionColor1(twitMessage));
                twit.setTwitDate(twitTime);
                twit.setOwnerID(user.getUserId());
                TwitDB.insert(twit);

                insertHashtags(twit);

                ArrayList<User> mentionList = findMentions(twitMessage);        // get list of users who were mentioned
                if (!mentionList.isEmpty()) {
                    insertMentionToDB(twitTime, mentionList);
                }

//                ArrayList<Twit> twits = UserMentionDB.selectMentions(user.getUserId());
                ArrayList<Twit> twits = UserMentionDB.selectAllTwits(user);
                session.setAttribute("twits", twits);

                session.setAttribute("numbTwits", TwitDB.countTwit(user.getUsername()));

            }

        } else if (action.equals("deleteTwit")) {
            url = "/home.jsp";
            String button = request.getParameter("btn");
            if (button != null || !button.equals("")) {
                TwitDB.delete(Integer.parseInt(button));

                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");

//                ArrayList<Twit> twits = UserMentionDB.selectMentions(user.getUserId());
                ArrayList<Twit> twits = UserMentionDB.selectAllTwits(user);

                session.setAttribute("twits", twits);
                session.setAttribute("numbTwits", TwitDB.countTwit(user.getUsername()));
            }

        } else if (action.equals("home")) {
            url = "/home.jsp";
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
//
//            ArrayList<Twit> twits = UserMentionDB.selectMentions(user.getUserId());
//            session.setAttribute("twits", twits);
            ArrayList<Twit> twits = UserMentionDB.selectAllTwits(user);
            session.setAttribute("twits", twits);
        } else if (action.equals("notifications")) {
            url = "/notifications.jsp";
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            ArrayList<Twit> lastMentions = UserMentionDB.selectLastMentions(user.getUsername(), user.getLastLoginTime());

            request.setAttribute("lastMentions", lastMentions);
            ArrayList<User> followers = FolowDB.selectFolowers(user);

            session.setAttribute("followers", followers);

        } else if (action.equals("follow")) {

            url = "/home.jsp";

            HttpSession session = request.getSession();
            User currUser = (User) session.getAttribute("user");
            String username = request.getParameter("username");
            User followedUser = UserDB.select(username);

            String button = request.getParameter("follow");

            // check if user clicked whether follow or unfollow button
            if (button != null && button.equals("follow")) {
                // add the followed user to database if he was never added before
                if (FolowDB.isFollowedUserInserted(currUser, followedUser) == false) {
                    FolowDB.insert(currUser, followedUser, getCurrentTime());
                }
            } else { // delete user from followed list

                FolowDB.delete(followedUser);

            }

            // get list of followed users and then add it to the session
            HashMap<String, User> followedUsers = FolowDB.selectFolowedUsers1(currUser);
            session.setAttribute("followedUsers", followedUsers);

            int following = FolowDB.countFollowing(currUser);
            session.setAttribute("numbFollowing", following);

            ArrayList<Twit> twits = UserMentionDB.selectAllTwits(currUser);
            session.setAttribute("twits", twits);

        } else if (action.equals("hashtag")) {
            url = "/hashtag.jsp";

            String hashName = request.getParameter("hashName");
            Hashtag hashtag = HashtagDB.search(hashName);

            if (hashtag != null) {
                ArrayList<Twit> twitsWithTags = HashtagDB.selectTwitsWithHashtags(hashtag);
                request.setAttribute("twitsWithTags", twitsWithTags);
            }

        }

        getServletContext().getRequestDispatcher(url).forward(request, response);

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "login.jsp";
        }

        String url = "";
        String message = "";

        switch (action) {

            case "signup": {
                url = "/signup.jsp";

                try {
                    // the function returns an error message if one of user's inputs is not valid
                    message = signup(request);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(membershipServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                User user = null;
                if (message.equals("") || message.isEmpty()) {

                    url = "/home.jsp";
                    user = (User) request.getAttribute("user");
                    String email = user.getEmailAddress();
                    user = UserDB.search(email);

                    // store the User object as a session attribute
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("users", UserDB.selectUsers());
                    session.setAttribute("trends", HashtagDB.selectTrends());
                }

                request.setAttribute("message", message);
//                request.setAttribute("user", user);
                
                
                break;
            }
            case "login": {
                url = "/login.jsp";

                String emailUsername = request.getParameter("username_email");
                String rememberMe = request.getParameter("rememberMe");

                User user = UserDB.select(emailUsername);
//                String loginTime = getCurrentTime();
//                user.setLastLoginTime(loginTime);
//                UserDB.insertLoginTime(loginTime, user.getUsername());

                if (user == null) {
                    message = "login failed";
                } else {
                    message = validateLoginInput(request, user);                    // the function returns error message if user's credential is not valid

                    if (message.equals("")) {

                        // store the User object as a session attribute
                        HttpSession session = request.getSession();
                        session.setAttribute("user", user);

//                        ArrayList<Twit> twits = UserMentionDB.selectMentions(user.getUserId());
                        ArrayList<Twit> twits = UserMentionDB.selectAllTwits(user);

                        session.setAttribute("twits", twits);
                        session.setAttribute("users", UserDB.selectUsers());    // for displaying list of users in the 3rd column
                        session.setAttribute("numbTwits", TwitDB.countTwit(user.getUsername()));

                        HashMap<String, User> followedUsers = FolowDB.selectFolowedUsers1(user);
                        session.setAttribute("followedUsers", followedUsers);

                        session.setAttribute("numbFollowing", FolowDB.countFollowing(user));        // get no of users who current user following  
                        session.setAttribute("numbFollowers", FolowDB.countFollowers(user));        // get no of followers
                        
                        ArrayList<String>trends = HashtagDB.selectTrends();
                        session.setAttribute("trends", HashtagDB.selectTrends());
                        url = "/home.jsp";
                    }

                    // if "remember me" checkbox is selected, create cookie
                    if (isCheckboxChecked(rememberMe)) {
                        Cookie c = new Cookie("email", emailUsername);
                        c.setMaxAge(60 * 60 * 24 * 365 * 2);                        // set age to 2 years
                        c.setPath("/");                                             // allow entire app to access it
                        response.addCookie(c);
                    }
                }

                break;
            }

            case "update": {
                url = "/signup.jsp";

                try {
                    // the function returns an error message if one of user's inputs is not valid
                    message = update(request);
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(membershipServlet.class.getName()).log(Level.SEVERE, null, ex);
                }

                if (message.equals("") || message.isEmpty()) {
                    url = "/home.jsp";
                }

                HttpSession session = request.getSession();
                User user = (User) session.getAttribute("user");

                String username = user.getUsername();
                user = UserDB.select(username);

                // get latest twits and mentions
                ArrayList<Twit> twits = UserMentionDB.selectMentions(user.getUserId());

                session.setAttribute("user", user);

                request.setAttribute("twits", twits);
                request.setAttribute("message", message);

                break;
            }

            case "forgotPassword": {
                url = "/forgotpassword.jsp";

                String email = request.getParameter("email");
                String questionNo = request.getParameter("securityquestion");
                String answer = request.getParameter("answer");

                User user = UserDB.select(email);

                if (user == null || email.equals("") || questionNo.equals("") || answer.equals("")) {
                    message = "information incorrect";

                } else {
                    if (user.getQuestionNo() == Integer.parseInt(questionNo) && user.getAnswer().equals(answer)) {
                        message = "found";

                        String newPassword = generatePassword(8);

//                         send email to user
                        String to = email;
                        String from = "yah@yahoo.com";
                        String subject = "Reset Password";

                        // returns error if email was not sent successful
                        // if there is no error, update user password
                        if (sendEmail(to, from, subject, newPassword).equals("")) {
                            try {
                                String salt = PasswordUtil.getSalt();
                                user.setPassword(PasswordUtil.hashAndSaltPassword(newPassword, salt));
                                UserDB.update(user);
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(membershipServlet.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    } else {
                        message = "Information incorrect";
                    }
                }

                break;
            }
        }

        request.setAttribute("message", message);
        getServletContext().getRequestDispatcher(url).forward(request, response);

    }

    private ArrayList<String> getSignUpValues(HttpServletRequest request, boolean signup) throws IOException, ServletException {
        ArrayList<String> userInfo = new ArrayList<>();
        userInfo.add(request.getParameter("fullname"));         // 0
        userInfo.add(request.getParameter("username"));         // 1
        userInfo.add(request.getParameter("email"));            // 2
        userInfo.add(request.getParameter("password"));         // 3
        userInfo.add(request.getParameter("confirmpassword"));  // 4
        userInfo.add(request.getParameter("dateofbirth"));      // 5
        userInfo.add(request.getParameter("securityquestion")); // 6
        userInfo.add(request.getParameter("answer"));           // 7

        String avatar = getProfileImage(request);
        if (avatar.equals("") && signup) {                  // signup
            userInfo.add("cat1.jpg");
        } else if (avatar.equals("") && !signup) {           // update
            User user = UserDB.search(request.getParameter("email"));
            userInfo.add(user.getProfilePicture());

        } else {
            userInfo.add(avatar);                 //  8 profile picture
        }
        return userInfo;
    }

    private String getProfileImage(HttpServletRequest request) throws IOException, ServletException {

        // get profile picture from the form
        String appPath = request.getServletContext().getRealPath("");
        // constructs path of the directory to save uploaded file
        String savePath = appPath + File.separator + "lib";

        // creates the save directory if it does not exists
        File fileSaveDir = new File(savePath);
        if (!fileSaveDir.exists()) {
            fileSaveDir.mkdir();
        }

        Part part = request.getPart("imgFile");
        String fileType = part.getContentType();
        long size = part.getSize();
        if (size != 0 || fileType.startsWith("img")) {

            // refines the fileName in case it is an absolute path
            String fileName = ImageUtil.extractFileName(part);
            fileName = new File(fileName).getName();
            part.write(savePath + File.separator + fileName);

            return fileName;
        }

        return "";
    }

    private String validateSignUp(ArrayList<String> userInfo) {

        int pass1 = 3, pass2 = 4, email = 2;
        User user = UserDB.search(userInfo.get(email));

        String errMsg = checkUserValues(userInfo);

        if (!errMsg.isEmpty() || !errMsg.equals("")) {
            return (errMsg + " can not be empty");
        } else if (userInfo.get(pass1).equals(userInfo.get(pass2)) == false) {
            return "Password and confirm password did not match.";
        } else if (user != null) {
            return "Email address already exist.";
        } else {
            return "";
        }

    }

    private void reloadUserInput(HttpServletRequest request, ArrayList<String> signupValues) {

        int size = signupValues.size();
        String attr = "attr";
        for (int i = 0; i < size; i++) {
            request.setAttribute(attr + i, signupValues.get(i));
        }
    }

    private String checkUserValues(ArrayList<String> values) {
        String msg = "";
        int size = values.size();

        String[] errType = new String[size];
        errType[0] = "Fullname";
        errType[1] = "Username";
        errType[2] = "Email";
        errType[3] = "Password";
        errType[4] = "Confirm password";
        errType[5] = "Date Of Birth";
        errType[6] = "Select question";
        errType[7] = "Answer";

        for (int i = 0; i < size - 1; i++) {
            if (values.get(i) == "" || values.get(i).isEmpty()) {
                msg = errType[i];
                break;
            }
        }

        return msg;

    }

    private User createUser(ArrayList<String> attributes) {
        int questionNo = 6;
        User user = new User();
        user.setFullName(attributes.get(0));
        user.setUsername(attributes.get(1));
        user.setEmailAddress(attributes.get(2));
        user.setPassword(attributes.get(3));
        user.setBirthdate(attributes.get(5));
//        user.setQuestionNo(Integer.parseInt(attributes.get(6)));

        // check whether user selected one of the question from the combobox on signup page
        if (!attributes.get(questionNo).isEmpty() || attributes.get(questionNo) != "") {
            user.setQuestionNo(Integer.parseInt(attributes.get(questionNo)));
            user.setAnswer(attributes.get(7));
        }

        user.setProfilePicture(attributes.get(8));
        return user;
    }

    private boolean isCheckboxChecked(String checkbox) {
        if (checkbox == null) {
            checkbox = "off";
        }

        return checkbox.equals("on");
    }

    private String validateLoginInput(HttpServletRequest request, User user) {

        String message = "";
        String emailUsername = request.getParameter("username_email");
        String password = request.getParameter("password");

        try {

            String salt = UserDB.selectSalt(user.getUsername());
            password = PasswordUtil.hashAndSaltPassword(password, salt);

            boolean usernameValid = (user.getEmailAddress().equals(emailUsername) || user.getUsername().equals(emailUsername));

            if (usernameValid && user.getPassword().equals(password)) {

                return message;

            } else {
                message = "username or password is invalid";
            }
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(membershipServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        return message;
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>/-+*

    private String deleteCookies(HttpServletRequest request,
            HttpServletResponse response) {

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            cookie.setMaxAge(0); //delete the cookie
            cookie.setPath("/"); //allow the download application to access it
            response.addCookie(cookie);
        }
        String url = "/login.jsp";
        return url;
    }

    private String signup(HttpServletRequest request) throws IOException, ServletException, NoSuchAlgorithmException {
        String errorMsg = request.getParameter("errorMsg");

        ArrayList<String> userInfo = getSignUpValues(request, true);      // collect user's input from signup.jsp

        errorMsg = validateSignUp(userInfo);                         // validate all user signup values

        if (errorMsg.equals("") || errorMsg.isEmpty()) {
            User user = createUser(userInfo);
            String salt = PasswordUtil.getSalt();
            String password = user.getPassword();
            user.setSalt(salt);

            user.setPassword(PasswordUtil.hashAndSaltPassword(password, salt));

            UserDB.insert(user);
            request.setAttribute("user", user);
            errorMsg = "";

        } else {
            reloadUserInput(request, userInfo);
        }
        return errorMsg;
    }

    private String update(HttpServletRequest request) throws IOException, ServletException, NoSuchAlgorithmException {
        String errorMsg = request.getParameter("errorMsg");

        ArrayList<String> userInfo = getSignUpValues(request, false);      // collect user's input from signup.jsp

        errorMsg = validateSignUp(userInfo);                         // validate all user signup values

        if (errorMsg.equals("") || errorMsg.isEmpty() || errorMsg.contains("Email")) { // ignore if user is found in db
            User user = createUser(userInfo);

            String salt = UserDB.selectSalt(user.getUsername());
            String password = user.getPassword();

            user.setPassword(PasswordUtil.hashAndSaltPassword(password, salt));
            UserDB.update(user);

            errorMsg = "";

        } else {
            reloadUserInput(request, userInfo);
        }

        return errorMsg;
    }

    private String generatePassword(int len) {
        String dic = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();

        String newPassword = "";
        for (int i = 0; i < len; i++) {
            int index = random.nextInt(dic.length());
            newPassword += dic.charAt(index);

        }

        return newPassword;

    }

    private String sendEmail(String to, String from, String subject, String newPassword) {

        boolean isBodyHTML = false;
        int index = to.indexOf("@");

        String emailType = to.substring(index + 1);

        try {
//            if (emailType.equals("yahoo.com")) {
            MailUtilYahoo.sendMail(to, from, subject, newPassword);
//            } else {
//                MailUtilGmail.sendMail(to, from, subject, newPassword);
//            }

        } catch (MessagingException e) {
            System.err.println(e.getMessage());
            return e.getMessage();
        }

        return "";
    }

    private void insertMentionToDB(String twitDate, ArrayList<User> mentionList) {
        Twit t = TwitDB.selectTwitByDate(twitDate);
        for (User user : mentionList) {
            UserMentionDB.insert(t.getTwitId(), user.getUserId());
        }
    }

    private ArrayList<User> findMentions(String twit) {
        boolean flag = true;
        String newText = "";

        ArrayList<User> users = new ArrayList<>();

        for (String token : twit.split("@", 0)) {
            if (flag) {
                newText += token;
                flag = false;
            } else {
                User user = UserDB.select(token);

                if (user != null) {
                    users.add(user);
                }
            }

        }
        return users;
    }

    /*
    
    
            Each time a new tweet is inserted
             For each hashtag #x in the tweet 
                Check if #x exists in Hashtag table.
                    If not, insert a new record for #x
                    If Yes, update the count field for #x
                Insert a record in tweetHashtag table, using tweetID, and hashtagID
     */
    private void insertHashtags(Twit twit) throws IOException {

        twit = TwitDB.searchTwit(twit);

        // hashtagID, hashtagText, hashtagCount
        boolean flag = true;
        String newText = "";

        // make sure there is a hash symbol in the text
        if (twit.getTwitMessage().indexOf("#") == -1) {
            return;
        }

        Hashtag hashtag;
        String link = "";
        String twitMessage = twit.getTwitMessage();
        Matcher matcher = Pattern.compile("#s*(\\w+)").matcher(twitMessage);
        while (matcher.find()) {
            String hashName = matcher.group(1);
            link = "<a href=" + "membership?action=hashtag" + "\\&hashName=" + hashName + ">#" + hashName + "</a>";
            twitMessage = twitMessage.replaceAll("#" + hashName + "\\b", link);

            hashtag = HashtagDB.search(hashName);

            if (hashtag != null) {
                int count = hashtag.getHashtagCount();
                hashtag.setHashtagCount(++count);
                HashtagDB.updateCount(hashtag);

            } else {
                hashtag = new Hashtag();
                hashtag.setHashtagText(hashName);
                hashtag.setHashtagCount(1);
                HashtagDB.insert(hashtag);
            }
            HashtagDB.insertTwitHashtag(hashtag, twit);
        }

        twit.setTwitMessage(twitMessage);

        TwitDB.update(twit);
    }

    private HashMap<String, Hashtag> selectHashtags() {
        HashMap<String, Hashtag> maps = new HashMap<>();

        return maps;
    }

    private String changeMentionColor1(String text) {
        boolean flag = true;
        String newText = "";
        Twit t = TwitDB.selectTwitByDate(text);

        for (String token : text.split("@", 0)) {
            if (flag) {
                newText += token;
                flag = false;
            } else {

                newText += " <a class='mention'>@" + token + "</a>";
            }

        }
        return newText;
    }

    private String getCurrentTime() {
        SimpleDateFormat sdfr = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String currTime = sdfr.format(new GregorianCalendar().getTime());

        return currTime;
    }

}
