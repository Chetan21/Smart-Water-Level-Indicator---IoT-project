<?php
$temp="";
if(isset($_GET['dist'])) {
        $temp = ($_GET['dist']);
        $link=mysqli_connect("localhost","root", "root", "water_level_data");
                if(!$link)
                        die('Could not connect: ' . mysqli_connect_errno());
        $sql = "INSERT INTO level_table (level) VALUES ('$temp')";
        $result = mysqli_query($link, $sql);
}
if(isset($_GET['action'])){
        $temp = $_GET['action'];
}
echo $temp;

?>
