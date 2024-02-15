console.log("Hello world");
document.addEventListener("DOMContentLoaded", function () {
  // Initialize the calendar when the document is loaded
  var calendarEl = document.getElementById("calendar");
  var calendar = new FullCalendar.Calendar(calendarEl, {
    // Header title and buttons
    headerToolbar: {
      // title, prev, next, prevYear, nextYear, today
      left: "prev,next today",
      center: "title",
      right: "dayGridMonth,timeGridWeek,timeGridDay",
    },
    // First day of the week
    firstDay: 1, // 1: Monday
    // Show Saturdays and Sundays
    weekends: true,
    // Week mode (fixed, liquid, variable)
    weekMode: "fixed",
    // Show week numbers
    weekNumbers: false,
    // Initial display view
    initialView: "dayGridMonth",
    // Show all-day slot
    allDaySlot: true,
    // All-day slot title
    allDayText: "All day",
    // Slot time format
    slotLabelFormat: {
      hour: "numeric",
      minute: "2-digit",
      omitZeroMinute: false,
      meridiem: "short",
    },
    // Slot minutes
    slotDuration: "00:15:00",
    // Time interval to select
    snapDuration: "00:15:00",
    // Scroll start time
    scrollTime: "09:00:00",
    // Minimum time
    minTime: "06:00:00",
    // Maximum time
    maxTime: "20:00:00",
    // Display year
    year: 2012,
    // Display month
    month: 12,
    // Display day
    day: 31,
    // Time format
    timeFormat: "H:mm",
    // Column format
    columnHeaderFormat: {
      month: "ddd", // Month
      week: "ddd d", // 7(Mon)
      day: "dddd d", // 7(Mon)
    },
    // Title format
    titleFormat: {
      month: "yyyy M", // 2013 September
      week: "yyyy M d{ to }{yyyy M d}", // 2013 September 7 to 13
      day: "yyyy M d (ddd)", // 2013 September 7 (Tue)
    },
    // Button strings
    buttonText: {
      prev: "<", // <
      next: ">", // >
      prevYear: "<<", // <<
      nextYear: ">>", // >>
      today: "Today",
      month: "Month",
      week: "Week",
      day: "Day",
    },
    // Month names
    monthNames: [
      "January",
      "February",
      "March",
      "April",
      "May",
      "June",
      "July",
      "August",
      "September",
      "October",
      "November",
      "December",
    ],
    // Month abbreviations
    monthNamesShort: [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ],
    // Day names
    dayNames: [
      "Sunday",
      "Monday",
      "Tuesday",
      "Wednesday",
      "Thursday",
      "Friday",
      "Saturday",
    ],
    // Day abbreviations
    dayNamesShort: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
    // Selectable
    selectable: true,
    // Draw a placeholder when selecting
    selectHelper: true,
    // Auto unselect
    unselectAuto: true,
    // Elements exempt from auto unselection
    unselectCancel: "",
    // Events
    events: [
      {
        title: "event1",
        start: "2013-01-01",
      },
      {
        title: "event2",
        start: "2013-01-02",
        end: "2013-01-03",
      },
      {
        title: "event3",
        start: "2013-01-05T12:30:00",
        allDay: false, // will make the time show
      },
      {
        title: "event4",
        start: "2024-02-15T12:30:00",
        allDay: false, // will make the time show
      },
    ],
  });
  // Render the calendar
  calendar.render();
});
