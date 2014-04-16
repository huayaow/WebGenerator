<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>  
<!DOCTYPE html>

<html>
<head>
<!-- Generation main -->
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="author" content="wayne">
    
<title>Generation</title>
<link rel="stylesheet" type="text/css" href="css/bootstrap.css">

<style type="text/css">
    body {
	    padding-top: 20px;
	    padding-bottom: 40px;
    }

     /* Custom table color */  
     .table-striped tbody > tr:nth-child(odd) > td,
     .table-striped tbody > tr:nth-child(odd) > th {
         background-color: #b8e7ff;
     }
      /* Custon link color */
     .nav-tabs > li > a {
         color: #666666 ;
     } 
     .nav-tabs > .active > a {
         color: #000000 ;
     }
</style>
<link href="css/bootstrap-responsive.min.css" rel="stylesheet">
  
<script type="text/javascript">
var checkv = 0 ;
function selectALG() {
	if( checkv != 0 && document.getElementsByName("ALG.alg_version")[0].checked ) {
		$("#PSOConfig").collapse('hide') ;
		$("#GreedyConfig").collapse('show') ;
		checkv = 0 ;
	}
	if( checkv != 1 && document.getElementsByName("ALG.alg_version")[1].checked ) {
		$("#GreedyConfig").collapse('hide') ;
		$("#PSOConfig").collapse('show') ;
		checkv = 1 ;
	}
}

function valiform() {
	var x = document.getElementsByName("ALG.alg_version") ;
	if( x[0].checked ) {  // Greedy 
		$('#myModal').modal('show') ;
		document.getElementById("config").value = document.getElementById("grepeat").value ;
		return false ;
	}
	if( x[1].checked ) {  // PSO 
		document.getElementById("config").value = 
			document.getElementById("prepeat").value + ";" +
			document.getElementById("psize").value + ";" +
			document.getElementById("itime").value + ";" +
			document.getElementById("weight").value + ";" +
			document.getElementById("factor").value ;	
		return true ;
	}
	return false ;
	
}

</script>

</head>

<body>
<%
    String in1 = request.getParameter("rawdata");
    String in2 = request.getParameter("tway");
    session.setAttribute("rawdata", in1);
    session.setAttribute("tway", in2);
%>

<!-- nav -->      
<div class="navbar navbar-fixed-top navbar-static-top navbar-inverse">
<div class="navbar-inner">
<div class="container">
     <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
     </button>
    
    <!-- <a class="brand" href="#">Sig CT</a> -->
    <div class="nav-collapse collapse">
        <ul class="nav">
        <li class="dropdown active">
        <a href="#" class="dropdown-toggle" data-toggle="dropdown"><strong>Generation</strong><b class="caret"></b></a>
        <ul class="dropdown-menu">
        <li class="active"><a href="#">Generation</a></li>
        <li><a href="#">Location</a></li>
        <li><a href="#">Application</a></li>
        <li><a href="/CombPublication">Publication</a></li>
        <!--  <li class="divider"></li>
        <li class="nav-header">Nav header</li> -->
        </ul>
    </li>
    </ul>
    
    <ul class="nav pull-right">
    <li><a href="/"><strong>Home</strong></a></li>
    <li><a href="/group.html"><strong>Group</strong></a></li>
    <li><a href="/contact.html"><strong>Contact</strong></a></li>
    <li><a href="#"><strong>About</strong></a></li>
    </ul>
    </div>
</div>
</div>
</div>

<div class="container">
    <br/><br/>
    <br/><br/>
    
    <!-- tab
    <ul class="nav nav-tabs">
       <li class="active"><a href="#">Generation</a></li>
       <li><a href="#">Location</a></li>
       <li><a href="#">Application</a></li>
       <li><a href="/CombPublication">Publication</a></li>
    </ul> -->
    
    <div class="row-fluid">   
        <div class="span2">
        <ul class="nav nav-list">
            <li class="nav-header">Step</li>
            <li>Set SUT</li> 
            <li class="active"><a href="#">Set Algorithm</a></li>
            <li>Covering Array</li>
        </ul>       
        </div>
        
        <div class="span10">
        <!-- show table form previous page -->
        <h4 class="well well-small">Software Under Test ( t-way = <%=session.getAttribute("tway")%> )</h4>
        <table id="ctable" class="table table-striped">
        <%
        String raw = (String)session.getAttribute("rawdata") ;
        String[] tp = raw.split(";");
        //cal the max column number
        String[] cal = tp ;
        int max = 0 ;
        for(int i=1; i<cal.length; i++ ) {
        	String[] cal_temp = cal[i].split(",") ;
        	if( cal_temp.length > max ) {
        		max = cal_temp.length ;
        	}
        }
        
        //show
        for(int i=1; i<tp.length; i++ ) {
        	String[] temp = tp[i].split(",") ;
        %>
        <tr>
        <%
        	for(int j=0; j<temp.length; j++) {
        		String ts = temp[j] ;
        %>
        <td><%=ts%></td>
        <%
            }
            if( temp.length < max ) {
            	for(int k=temp.length; k<max; k++) {
        %>
         <td>  </td>
        <%    		
            	}
            }
        %>	
        </tr>	
        <%
        }
        %>
        </table>

        <!-- algorithm choose -->
        <form name="algform" id="algorithm" class="well weill-large" 
        action="GenProcess.action" onsubmit="return valiform()" method="post">
           <div class="control-group">
               <!-- greedy -->
               <label class="radio" style="font-size:19px">
               <input type="radio" name="ALG.alg_version" id="algorithm1" value="Greedy" onclick="selectALG()" checked>
               <a href="#" rel="tooltip" title="greedy algorithm is ...">Greedy Algorithm</a>
               </label>
               <div id="GreedyConfig" class="collapse in">
               <div class="row-fluid">
                   <div class="span4">
                     <label>repeat times</label>
                     <input type="text" name="grepeat" id="grepeat" value="10"> 
                   </div>
               </div>
               </div>
               
               <!-- PSO -->
               <br/>
               <label class="radio" style="font-size:19px">
               <input type="radio" name="ALG.alg_version" id="algorithm2" value="PSO" onclick="selectALG()">
               <a href="#" rel="tooltip" title="Particle swarm optimization is one of the swarm based evolutionary computation techniques. It was first developed by Kennedy and Eberhart, which was inspired by the social behavior of bird flocking and fish schooling.">
               Particle Swarm Optimization</a>
               </label>
               <div id="PSOConfig" class="collapse">
               <div class="row-fluid">
                   <div class="span4">
                     <label>repeat time</label>
                     <input type="text" name="prepeat" id="prepeat" value="10"> 
                   </div>
                   <div class="span4">
                     <label>population size</label>
                     <input type="text" name="psize" id="psize" value="100"> 
                   </div>
                   <div class="span4">
                     <label>iteration times</label>
                     <input type="text" name="itime" id="itime" value="150"> 
                   </div>
               </div>
               <div class="row-fluid">
                  <div class="span4">
                     <label>inertia weight</label>
                     <input type="text" name="weight" id="weight" value="0.9"> 
                   </div>  
                  <div class="span4">
                     <label>acceleration constant</label>
                     <input type="text" name="factor" id="factor" value="1.7"> 
                   </div>
               </div>
               </div>
     
               <br/> 
               <div class="control-group-button">
                  <div class="controls">
                     <input type="hidden" name="ALG.alg_config" id="config" value="10">
                     <button type="submit" class="btn btn-primary">Next</button>
                  </div>
               </div>
             </div>   
         </form>    
               
      </div> <!-- /span9 -->    
    </div> <!-- /row-fluid -->
    
    <hr>
    <div class="footer">
      <p>&copy; Company 2012</p>
    </div>
    
</div> <!-- /container -->

<!-- modal -->
<div class="modal hide fade" id="myModal">
  <div class="modal-header">
    <h3>Attention</h3>
  </div>
  <div class="modal-body">
    <p>This algorithm is not supported now, please choose another algorithm</p>
  </div>
  <div class="modal-footer">
    <a href="#" class="btn btn btn-danger" data-dismiss="modal">OK</a>
  </div>
</div>

<script src="js/jquery-1.8.3.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/bootstrap-tooltip.js"></script>
<script src="js/bootstrap-modal.js"></script>
<script src="js/bootstrap-collapse.js"></script>
<!--<script src="http://wrongwaycn.github.com/bootstrap/docs/assets/js/jquery.js"></script>
<script src="http://wrongwaycn.github.com/bootstrap/docs/assets/js/bootstrap-tooltip.js"></script>-->
</body>
</html>