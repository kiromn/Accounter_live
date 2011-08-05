<html>
  <head>
        <meta content="IE=100" http-equiv="X-UA-Compatible">
		<link rel="shortcut icon" href="../images/favicon.ico" />
		<% String version = application.getInitParameter("version"); %>
		<link type="text/css" href="../css/ss.css?version=<%= version%>" rel="stylesheet">
  </head>
    <body>
	    <div id="commanContainer">
		 <img src="../images/Accounter_logo_title.png" class="accounterLogo" />
		 <form action="/activation" method="post">
		    <div class="reset-header">
			  <!-- <h2>Reset Activation Code</h2>-->
			</div>
			<div>
			  <label>Enter valid activation code</label>
			  <input type="text" name="code">
			</div>
			<div class="reset-button">
			   <input type="submit" tabindex="3" value="Activate" name="activate" class="allviews-common-button" id="submitButton">
			</div>
		 </form>
     </div>
	</body>
</html>