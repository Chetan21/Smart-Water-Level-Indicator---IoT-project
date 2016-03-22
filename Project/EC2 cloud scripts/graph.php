<?php
        $link = mysqli_connect("localhost","root","root","water_level_data");
        if(!$link)
                die('could not connect: ');
        $sql = "SELECT * FROM level_table";
        $result = mysqli_query($link, $sql);
        $num = mysqli_num_rows($result);
        $ans = "";
        for($i=0; $i<$num; $i++){
                $row = mysqli_fetch_assoc($result);
                $ans .= $row["level"]."#".$row["time_stamp"]."#";
        }
        echo $ans;
?>