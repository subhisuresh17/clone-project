$(document).ready(function () {
  // Wait until the document is loaded and initialize the calendar.
  $("#calendar").fullCalendar({
    // Header title and buttons
    header: {
      // title, prev, next, prevYear, nextYear, today
      left: "prev,next today",
      center: "title",
      right: "month agendaWeek agendaDay",
    },
    // jQuery UI theme
    theme: false,
    // First day of the week
    firstDay: 1, // 1: Monday
    // Show Saturdays and Sundays
    weekends: true,
    // Week mode (fixed, liquid, variable)
    weekMode: "fixed",
    // Show week numbers
    weekNumbers: false,
    // Height(px)
    //height: 700,
    // Content height(px)
    //contentHeight: 600,
    // Calendar aspect ratio (as the ratio increases, the height shrinks)
    //aspectRatio: 1.35,
    // View display event
    viewDisplay: function (view) {
      //alert('View display event ' + view.title);
    },
    // Window resize event
    windowResize: function (view) {
      //alert('Window resize event');
    },
    // Date click event
    dayClick: function () {
      //alert('Date click event');
    },
    // Initial display view
    defaultView: "month",
    // Show all-day slot
    allDaySlot: true,
    // All-day slot title
    allDayText: "All day",
    // Slot time format
    axisFormat: "H(:mm)",
    // Slot minutes
    slotMinutes: 15,
    // Time interval to select
    snapMinutes: 15,
    // TODO Not sure
    //defaultEventMinutes: 120,
    // Scroll start time
    firstHour: 9,
    // Minimum time
    minTime: 6,
    // Maximum time
    maxTime: 20,
    // Display year
    year: 2012,
    // Display month
    month: 12,
    // Display day
    day: 31,
    // Time format
    timeFormat: "H(:mm)",
    // Column format
    columnFormat: {
      month: "ddd", // Month
      week: "d'('ddd')'", // 7(Mon)
      day: "d'('ddd')'", // 7(Mon)
    },
    // Title format
    titleFormat: {
      month: "yyyy M", // 2013 September
      week: "yyyy M d{ to }{[yyyy]}{[M]} d", // 2013 September 7 to 13
      day: "yyyy M d (ddd)", // 2013 September 7 (Tue)
    },
    // Button strings
    buttonText: {
      prev: "&lsaquo;", // <
      next: "&rsaquo;", // >
      prevYear: "&laquo;", // <<
      nextYear: "&raquo;", // >>
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
    // Event sources
    eventSources: [
      {
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
            start: "2013-01-05 12:30:00",
            allDay: false, // will make the time show
          },
          {
            title: "event3",
            start: "2024-02-15 12:30:00",
            allDay: false, // will make the time show
          },
        ],
      },
    ],
  });
  // Dynamically change options
  //$('#calendar').fullCalendar('option', 'height', 700);

  // Render the calendar. Used for switching display.
  //$('#calendar').fullCalendar('render');

  // Destroy the calendar (also destroys event handlers and internal data).
  //$('#calendar').fullCalendar('destroy')
});
