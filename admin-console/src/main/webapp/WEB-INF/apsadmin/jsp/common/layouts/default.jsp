<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>
<%@ taglib prefix="wp" uri="/aps-core" %>
<%@ taglib prefix="wpsa" uri="/apsadmin-core" %>
<!DOCTYPE html>
<!--[if IE 9]><html lang="en-us" class="ie9 layout-pf layout-pf-fixed"><![endif]-->
<!--[if gt IE 9]><!-->
<html lang="en-us" class="layout-pf layout-pf-fixed">
<!--<![endif]-->

<head>
  <title>Entando - <s:set var="documentTitle"><tiles:getAsString name="title"/></s:set><s:property value="%{getText(#documentTitle)}" escape="false" /></title>
  <meta charset="UTF-8">
  <meta http-equiv="X-UA-Compatible" content="IE=Edge">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="shortcut icon" href="patternfly/img/favicon.ico">

  <link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly.min.css">
  <link rel="stylesheet" href="<wp:resourceURL />administration/patternfly/css/patternfly-additions.min.css">
  <link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-default-theme.css">
  <link rel="stylesheet" href="<wp:resourceURL />administration/css/entando-admin-console-blue-theme.css">

  <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44="
    crossorigin="anonymous"></script>
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="http://cdnjs.cloudflare.com/ajax/libs/jquery.matchHeight/0.7.0/jquery.matchHeight-min.js"></script>
    <script src="<wp:resourceURL />administration/patternfly/js/patternfly.js"></script>

    <script>
      $(document).ready(function () {
        // matchHeight the contents of each .card-pf and then the .card-pf itself
        $(".row-cards-pf > [class*='col'] > .card-pf .card-pf-title").matchHeight();
        $(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-body").matchHeight();
        $(".row-cards-pf > [class*='col'] > .card-pf > .card-pf-footer").matchHeight();
        $(".row-cards-pf > [class*='col'] > .card-pf").matchHeight();

        // Initialize the vertical navigation
        $().setupVerticalNavigation(true);
      });
    </script>
</head>

<body>

  <!--barra di navigazione-->
  <nav class="navbar navbar-pf-vertical">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle">
      <span class="sr-only">Toggle navigation</span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
      <span class="icon-bar"></span>
    </button>
      <a href="/" class="navbar-brand">
        <img class="navbar-brand-icon logo-entando" src="<wp:resourceURL />administration/img/entando-logo.svg" alt="Entando 4.3" />
      </a>
    </div>
    <nav class="collapse navbar-collapse">

      <ul class="nav navbar-nav navbar-right navbar-iconic">
        </li>
        <li class="dropdown">
          <a class="dropdown-toggle nav-item-iconic" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            <span title="Help" class="fa pficon-help"></span>
            <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" aria-labelledby="dropdownMenu1">
            <li><a href="#">Help</a></li>
            <!--<li><a href="#">About</a></li>-->
          </ul>
        </li>
        <li class="dropdown">
          <a class="dropdown-toggle nav-item-iconic" id="dropdownMenu2" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">
            <span title="Username" class="fa pficon-user"></span>
            <span class="caret"></span>
          </a>
          <ul class="dropdown-menu" aria-labelledby="dropdownMenu2">
            <li><a href="#">My profile</a></li>
            <li><a href="#">Logout</a></li>
          </ul>
        </li>
      </ul>
    </nav>
  </nav>
  <!--/.navbar-->

  <!--barra di menu laterale-->
  <div class="nav-pf-vertical nav-pf-vertical-with-sub-menus nav-pf-vertical-collapsible-menus ">
    <ul class="list-group">
      <li class="list-group-item">
        <a>
          <span class="fa fa-files-o" data-toggle="tooltip" title="Dashboard"></span>
          <span class="list-group-item-value">Page Designer</span>
        </a>
      </li>
      <li class="list-group-item">
        <a>
          <span class="fa fa-object-ungroup" data-toggle="tooltip" title="Dolor"></span>
          <span class="list-group-item-value">UX Patterns</span>
        </a>
      </li>

      <li class="list-group-item active secondary-nav-item-pf" data-target="#ipsum-secondary">
        <a>
          <span class="fa fa-cubes" data-toggle="tooltip" title="Ipsum"></span>
          <span class="list-group-item-value">Integrations</span>
        </a>
        <!--menu secondo livello di 1 livello-->

        <div id="-secondary" class="nav-pf-secondary-nav">
          <div class="nav-item-pf-header">
            <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
            <span>titolo 1 livello</span>
          </div>

          <!--menu secondo livello aperto -->

          <ul class="list-group">
            <li class="list-group-item active tertiary-nav-item-pf" data-target="#ipsum-intellegam-tertiary">
              <a>
                <span class="list-group-item-value">2 livello</span>
              </a>

              <div id="compute-containers-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>titolo terzo livello</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item active">
                    <a>
                      <span id="compute-containers-users-nav-item" class="list-group-item-value">3 livello a</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span id="compute-containers-groups-nav-item" class="list-group-item-value">3 livello b</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span id="compute-containers-roles-nav-item" class="list-group-item-value">4 livello c</span>

                    </a>
                  </li>
                </ul>
              </div>


            </li>
            <li class="list-group-item tertiary-nav-item-pf" data-target="#ipsum-copiosae-tertiary">
              <a>
                <span class="list-group-item-value">Copiosae</span>
              </a>

              <div id="compute-infrastructure-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>Copiosae</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Exerci</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Quaeque</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Utroque</span>

                    </a>
                  </li>
                </ul>
              </div>


            </li>
            <li class="list-group-item tertiary-nav-item-pf" data-target="#ipsum-patrioque-tertiary">
              <a>
                <span class="list-group-item-value">Patrioque</span>
              </a>

              <div id="compute-clouds-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>Patrioque</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Novum</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Pericula</span>
                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Gubergren</span>
                    </a>
                  </li>
                </ul>
              </div>


            </li>

            <li class="list-group-item">
              <a>
                <span class="list-group-item-value">Accumsan</span>

              </a>
            </li>

          </ul>
          <!--fine menu livello-->
        </div>
      </li>
      <li class="list-group-item secondary-nav-item-pf" data-target="#amet-secondary">
        <a>
          <span class="fa fa-users" data-toggle="tooltip" title="Amet"></span>
          <span class="list-group-item-value">Users Settings</span>
        </a>

        <div id="amet-secondary" class="nav-pf-secondary-nav">
          <div class="nav-item-pf-header">
            <a class="secondary-collapse-toggle-pf" data-toggle="collapse-secondary-nav"></a>
            <span>Amet</span>
          </div>
          <ul class="list-group">
            <li class="list-group-item tertiary-nav-item-pf" data-target="#amet-detracto-tertiary">
              <a>
                <span class="list-group-item-value">Detracto Suscipiantur</span>

              </a>

              <div id="amet-detracto-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>Detracto</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Delicatissimi</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Aliquam</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Principes</span>

                    </a>
                  </li>
                </ul>
              </div>


            </li>
            <li class="list-group-item tertiary-nav-item-pf" data-target="#amet-mediocrem-tertiary">
              <a>
                <span class="list-group-item-value">Mediocrem</span>

              </a>

              <div id="amet-mediocrem-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>Mediocrem</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Convenire</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Nonumy</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Deserunt</span>

                    </a>
                  </li>
                </ul>
              </div>


            </li>
            <li class="list-group-item tertiary-nav-item-pf" data-target="#amet-corrumpit-tertiary">
              <a>
                <span class="list-group-item-value">Corrumpit Cupidatat Proident Deserunt</span>

              </a>

              <div id="amet-corrumpit-tertiary" class="nav-pf-tertiary-nav">
                <div class="nav-item-pf-header">
                  <a class="tertiary-collapse-toggle-pf" data-toggle="collapse-tertiary-nav"></a>
                  <span>Corrumpit</span>
                </div>
                <ul class="list-group">
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Aeque</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Delenit</span>

                    </a>
                  </li>
                  <li class="list-group-item">
                    <a>
                      <span class="list-group-item-value">Qualisque</span>

                    </a>
                  </li>
                </ul>
              </div>


            </li>

            <li class="list-group-item">
              <a>
                <span class="list-group-item-value">Urbanitas Habitant Morbi Tristique</span>

              </a>
            </li>

          </ul>
        </div>
      </li>
      <li class="list-group-item">
        <a>
          <span class="fa fa-pencil-square-o" data-toggle="tooltip" title="Adipscing"></span>
          <span class="list-group-item-value">CMS</span>
        </a>
      </li>


      <!--fine primo livello-->

    </ul>

    <ul class="list-group fixed-bottom">
      <li class="list-group-item">
        <a>
          <span class="fa fa-cogs" data-toggle="tooltip" title="Adipscing"></span>
          <span class="list-group-item-value">Settings</span>
        </a>
      </li>
    </ul>
  </div>


  <div class="container-fluid container-cards-pf container-pf-nav-pf-vertical">

   caro eu
 
  </div>
</body>
</html>