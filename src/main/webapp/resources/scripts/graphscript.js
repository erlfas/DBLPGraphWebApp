var network;

var nodes = new vis.DataSet();
var edges = new vis.DataSet();
var gephiImported;

var data = {
    nodes: nodes,
    edges: edges
};

var options = {
    nodes: {
        shape: 'dot',
        radiusMin: 10,
        radiusMax: 30,
        fontSize: 12,
        fontFace: "Tahoma",
        scaleFontWithValue: true,
        fontSizeMin: 8,
        fontSizeMax: 20,
        fontThreshold: 12,
        fontSizeMaxVisible: 20
    },
    edges: {
        width: 0.15,
        inheritColor: "from",
        fontSize: 12,
        fontFace: "Tahoma",
        scaleFontWithValue: true,
        fontSizeMin: 8,
        fontSizeMax: 20,
        fontThreshold: 12,
        fontSizeMaxVisible: 20,
        fontStrokeWidth: 1,
        fontStrokeColor: "#d1d1d1",
        fontFill: "none"
    },
    tooltip: {
        delay: 200,
        fontSize: 12,
        color: {
            background: "#fff"
        }
    },
    smoothCurves: {dynamic: false, type: "continuous"},
    stabilize: false,
    physics: {barnesHut: {gravitationalConstant: -10000, springConstant: 0.002, springLength: 150}},
    hideEdgesOnDrag: true
};

function init() {
    var authorName = document.getElementById('name').value;
    alert("author: " + authorName);
    loadJSON("http://localhost:8080/DBLPGraph/webresources/Graph/" + authorName, redrawAll);
    var container = document.getElementById('visualization');
    network = new vis.Network(container, data, options);
    alert("init slutt");
};

/**
 * This function fills the DataSets. These DataSets will update the network.
 */
function redrawAll(gephiJSON) {
    
    alert("inni redrawAll");
    
    if (gephiJSON.nodes === undefined) {
        gephiJSON = gephiImported;
    } else {
        gephiImported = gephiJSON;
    }

    nodes.clear();
    edges.clear();

    var parsed = vis.network.gephiParser.parseGephi(gephiJSON, {allowedToMove: true, parseColor: true});

    // add the parsed data to the DataSets.
    nodes.add(parsed.nodes);
    edges.add(parsed.edges);

    var data = nodes.get(2); // get the data from node 2
    nodeContent.innerHTML = syntaxHighlight(data); // show the data in the div
    network.zoomExtent(); // zoom to fit
}

// from http://stackoverflow.com/questions/4810841/how-can-i-pretty-print-json-using-javascript
function syntaxHighlight(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
};

function loadJSON(path, success, error) {
    var xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4) {
            if (xhr.status === 200) {
                success(JSON.parse(xhr.responseText));
            } else {
                error(xhr);
            }
        }
    };
    xhr.open("GET", path, true);
    xhr.send();
};