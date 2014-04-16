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
     .r-table-striped tbody > tr:nth-child(odd) > td,
     .r-table-striped tbody > tr:nth-child(odd) > th {
         background-color: #cde6c7;
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

<script src="js/jquery-1.8.3.js"></script>
<script type="text/javascript">
<%
String cov = (String)request.getAttribute("ChartCov") ;
int covmax = (Integer)request.getAttribute("ChartMax") ;
%>
$(function () {
    var chart ;
    
    // format data 
    var t2 = "<%=cov%>"
    var t2m = <%=covmax%> ;
    var a22 = t2.split(",") ; 
    var a2 = new Array(a22.length);
    for(var p=0; p<a22.length; p++)
    	a2[p] = parseInt(a22[p]) / t2m ;
    var inter = 1 ;
    if(a22.length>30)
    	inter = 2 ;
    else if(a22.length>90)
    	inter = 5 ;
    
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'container-chart',
                type: 'area'
            },
            title: {
                text: null
            },
            subtitle: {
                text: null
            },
            xAxis: {
            	tickInterval: inter ,
                tickmarkPlacement: 'on',
                title: {
                    enabled: false
                }
            },
            yAxis: {
                title: {
                    text: null
                },
                max: 1.0
            },
            tooltip: {
                formatter: function() {
                    return ''+
                        'test case ' + this.x +', coverage '+ Highcharts.numberFormat(this.y*100, 1, '.', ',')+'%' ;
                }
            },
            plotOptions: {
                area: {
                    stacking: 'normal',
                    lineColor: '#666666',
                    lineWidth: 1,
                    marker: {
                        lineWidth: 1,
                        lineColor: '#666666'
                    }
                }
            },
            series: [{
                name: 'coverage',
                data: a2 ,
                color: '#0088cc'
            }]
        });
    });
    
});
</script>
</head>

<body>  
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
    </ul>-->
    
    <div class="row-fluid">
        <div class="span2">
        <ul class="nav nav-list">
            <li class="nav-header">Step</li>
            <li>Set SUT</li> 
            <li>Choose Algorithm</li>
            <li class="active"><a href="#">Result</a></li>
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
        
        <!-- show algorithm -->
        <div class="well well-small">
            <h4>Generation Algorithm: <s:property value="ALG.alg_version"/></h4>
            <%
            String ver = (String)request.getParameter("ALG.alg_version");
            if(ver.equals("Greedy")) {
            %>
            <p>repeat time: <s:property value="ALG.alg_config"/><p>
            <%
            }
            if(ver.equals("PSO")) {
        	     String con = request.getParameter("ALG.alg_config");
        	     String[] c = con.split(";");	
            %>
            <p>repeat time: <%=c[0]%></p>
            <p>population size: <%=c[1]%></p>
            <p>iteration time: <%=c[2]%></p>
            <p>inertia weight: <%=c[3]%></p>
            <p>acceleration constant: <%=c[4]%></p>
            <% 
            }
            %>
        </div>

        <!-- show result -->
        <div class="well well-small">
        <h4>Result<br/></h4>
        <p>the size of covering array is <strong><font size="3" color="#102b6a"><%=request.getAttribute("GenSize")%></font></strong>, 
        and the time cost is <strong><font size="3" color="#102b6a"><%=request.getAttribute("GenTime")%></font></strong> millisecond</p>
        </div>
        <table id="ctable" class="table r-table-striped">
        <%
        String data = (String)request.getAttribute("GenResult");
        String[] temp = data.split(";");
        // show parameter
        String[] row = temp[0].split(",");
        %>
        <thead><tr>
        <% 
        for(int k=0; k<row.length; k++) {
        %>
        <td><%=row[k]%></td>
        <% 
        }
        %>
        </tr></thead>
        
        <% 
        // for each row
        for(int x=1; x<temp.length; x++) {
        	row = temp[x].split(",");
        %>
        <tr>
        <%	
        	for(int y=0; y<row.length; y++) {
        %>
        <td><%=row[y]%></td>   		
        <%		
        	}
        %>
        </tr>
        <%
        }
        %>
        </table>
        
        <div class="well well-small">
        <h4>Coverage Analysis<br/></h4>
        </div>
        <div id="container-chart" style="min-width: 400px; height: 400px; margin: 0 auto"></div>
        
        
        </div>  <!-- span9 -->

   
    </div> <!-- row-fluid -->
    
    
    <hr>
    <div class="footer">
      <p>&copy; Company 2012</p>
    </div>
    
</div> <!-- /container -->

<script src="js/jquery-1.8.3.js"></script>
<script src="js/bootstrap.js"></script>
<script src="js/highcharts.js"></script>
<script src="js/exporting.js"></script>
</body>
</html>