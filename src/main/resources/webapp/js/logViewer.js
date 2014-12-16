$(function() {
    $("#date").datepicker({ dateFormat: 'yy-mm-dd' }).val(new Date().toJSON().split("T")[0]);

    $("#btnSearch").click(function() {
        $("#logContainer").html('<img src="images/loading.gif" alt="Loading">');
        var url  = "/api/logs?date=" + $("#date").val() + "&srcIp=" + $("#srcIp").val() + "&dstIp=" + $("#dstIp").val() + "&eventType=" + $("#eventType").val();
        $.get(url).success(function (data) {
            if (data != "") {
                $("#logContainer").html(data);
            } else {
                $("#logContainer").html("No data for current selection.");
            }
        });
        return false;
    });
});