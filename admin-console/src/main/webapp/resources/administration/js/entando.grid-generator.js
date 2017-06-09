
(function(context){


    /**
     * @constructor
     * @param {Object} options - the options
     * @param {Array} options.frames - an array of frames
     * @param {number} options.rowHeight - the row height in pixels
     */
    function GridGenerator(options) {

        var me = this;

        if (_.isEmpty(options.frames)) {
            throw this.newError(GridGenerator.MISSING_FRAMES);
        }

        this.options = options;

        this.originalFrames = options.frames;

        var lastDefinedFrame = _.maxBy(options.frames, 'sketch.y2');
        this.totalBoundaries = {
            x1: 0,
            y1: 0,
            x2: 11,
            y2: _.get(lastDefinedFrame, 'sketch.y2', -1)
        };

        this.totalFrames = _.map(options.frames, function(frame) {
            var internalFrame;
            if (frame.sketch) {
                internalFrame = _.clone(frame.sketch);
            } else {
                ++me.totalBoundaries.y2;
                internalFrame = {
                    x1: 0,
                    x2:11,
                    y1: me.totalBoundaries.y2,
                    y2: me.totalBoundaries.y2
                };

            }
            internalFrame.description = frame.description;
            internalFrame.pos = frame.pos;
            return internalFrame;
        });

        var malformed = this.detectMalformed(this.totalFrames);
        if (!_.isEmpty(malformed)) {
            throw this.newError(GridGenerator.ERROR.MALFORMED_FRAMES, malformed);
        }

        var overlaps = this.detectOverlaps(this.totalFrames);
        if (!_.isEmpty(overlaps)) {
            throw this.newError(GridGenerator.ERROR.OVERLAPPING_FRAMES, overlaps);
        }

    }

    GridGenerator.ERROR = {
        OVERLAPPING_FRAMES: 'OVERLAPPING_FRAMES',
        MALFORMED_FRAMES: 'MALFORMED_FRAMES',
        MISSING_FRAMES: 'MISSING_FRAMES'
    };

    var DIRECTION_ROW = 'row',
        DIRECTION_COL = 'col';


    /**
     * Returns all frames with the auto-generated missing sketch properties
     * @returns {Array}
     */
    GridGenerator.prototype.getUpdatedFrames = function getUpdatedFrames () {
        var frames = _.cloneDeep(this.originalFrames);
        for (var i=0; i<frames.length; ++i) {
            frames[i].sketch = _.pick(this.totalFrames[i], ['x1', 'x2', 'y1', 'y2']);
        }
        return frames;
    };

    /**
     * Returns all frames included in the frames param, that
     * are at the specified row/column
     * @param {string} errorType - the GridGenerator.ERROR type
     * @param {Object} data - the error data
     * @returns {Error} the custom error
     */
    GridGenerator.prototype.newError = function newError (errorType, data) {
        var err = new Error(errorType);
        err.type = errorType;
        err.data = data;
        return err;
    };

    /**
     * Returns all frames included in the frames param, that
     * are at the specified row/column
     * @param {Array} frames - the frames to be considered
     * @param {string} direction - is it a row or column index?
     * @param {number} index - the index
     * @returns {Array} a subset of the specified frames, overlapping the specified row (or column)
     */
    GridGenerator.prototype.getFramesAtIndex = function getFramesAtIndex (frames, direction, index) {
        return _.filter(frames, function(el) {
            return direction === DIRECTION_ROW ?
            el.y1 <= index && el.y2 >= index :
            el.x1 <= index && el.x2 >= index;
        });
    };


    /**
     * Returns all frames included in the frames param, that
     * are in the specified section (grid row or column)
     * @param {Array} frames - the frames to be considered
     * @param {string} direction - is it a grid row or column?
     * @param {Object} section - the section
     * @returns {Array} a subset of the specified frames, included in the specified grid row (or column)
     */
    GridGenerator.prototype.getFramesInSection = function getFramesInSection(frames, direction, section) {
        var framesInSection = [],
            p = (direction === DIRECTION_ROW ? 'y' : 'x'),
            p1 = p+1,
            p2 = p+2;

        for (var i=section[p1]; i<=section[p2]; ++i) {
            framesInSection.push(this.getFramesAtIndex(frames, direction, i));
        }
        return _.uniq(_.flatten(framesInSection));
    };



    /**
     * Returns a set of sections (grid rows or columns)
     * @param {Array} frames - the frames to be considered
     * @param {Object} boundaries - the area to be splitted in rows / columns
     * @param {string} direction - should we split in rows or columns?
     * @returns {Array} a set of sections (grid rows or columns)
     */
    GridGenerator.prototype.splitSections = function splitSections(frames, boundaries, direction) {

        var p1, p2, propsToPick;
        if (direction === DIRECTION_ROW) {
            p1 = 'y1';
            p2 = 'y2';
            propsToPick = ['x1', 'x2'];
        } else {
            p1 = 'x1';
            p2 = 'x2';
            propsToPick = ['y1', 'y2'];
        }

        var startIndex = boundaries[p1];
        var endIndex = boundaries[p2];


        var sections = [];
        var curSection = _.pick(boundaries, propsToPick);
        curSection[p1] = startIndex;


        for (var i=startIndex; i<endIndex; ++i) {
            var c1 = this.getFramesAtIndex(frames, direction, i);
            var c2 = this.getFramesAtIndex(frames, direction, i+1);
            var int = _.intersection(c1, c2);
            if (_.isEmpty(int)) {
                curSection[p2] = i;

                if (_.isEmpty(c1)) {
                    curSection.empty = true;
                }

                sections.push(curSection);

                // start new section
                curSection = _.pick(boundaries, propsToPick);
                curSection[p1] = i + 1;
            }
        }

        // add last section
        if (_.isEmpty(c2)){
            curSection.empty = true;
        }
        curSection[p2] = endIndex;
        sections.push(curSection);

        var fixedSections = [];
        for (var i=0; i<sections.length; ++i) {
            if (sections[i].empty) {
                var last = _.last(fixedSections);
                if (last && last.empty) {
                    last[p2] = sections[i][p2];
                    continue;
                }
            }
            fixedSections.push(sections[i]);
        }


        return fixedSections;
    };

    /**
     * Detects malformed frames
     */
    GridGenerator.prototype.detectMalformed = function detectMalform () {
        var malformed = [];

        _.forEach(this.totalFrames, function(frame) {
            var isOk = (
                _.inRange(frame.x1, 0, 12) &&
                _.inRange(frame.x2, 0, 12) &&
                frame.x1 <= frame.x2 &&
                frame.y1 <= frame.y2 &&
                frame.y1 >= 0
            );
            if (!isOk) {
                malformed.push(frame);
            }
        });
        return malformed;
    };

    /**
     * Detects overlapping frames
     * @returns {Array}
     */
    GridGenerator.prototype.detectOverlaps = function detectOverlaps() {
        var overlaps = [];
        var doOverlap = function(a, b) {
            // if one rectangle is on left side of other
            if (a.x1 > b.x2 || b.x1 > a.x2) {
                return false;
            }
            // if one rectangle is above other
            if (a.y1 > b.y2 || b.y1 > a.y2) {
                return false;
            }
            return true;
        };
        var frames = this.totalFrames;
        for (var i=0; i<frames.length-1; ++i) {
            for (var j=i+1; j<frames.length; ++j) {

                if( doOverlap(frames[i], frames[j])) {
                    overlaps.push({
                        a: frames[i],
                        b: frames[j]
                    });
                }
            }
        }
        return overlaps;
    };

    /**
     * Returns a tree representing the grid, with empty nodes or frames as leaves
     * @param {Array} frames - the frames to be considered in the recursion
     * @param {Object} boundaries - the area containing the subtree (the sub-grid)
     * @returns {Object} the root node of the tree
     */
    GridGenerator.prototype.getTree = function getTree(frames, boundaries) {

        var rows = this.splitSections(frames, boundaries, DIRECTION_ROW);


        for (var i=0; i<rows.length; ++i) {
            var frsInRow = this.getFramesInSection(frames, DIRECTION_ROW, rows[i]);
            var cols = this.splitSections(frsInRow, rows[i], DIRECTION_COL);

            rows[i].cols = cols;

            for (var j=0; j<cols.length; ++j) {

                var frsInCol = this.getFramesInSection(frsInRow, DIRECTION_COL, cols[j]);


                if (frsInCol.length === 0) {
                    continue;
                } else if (frsInCol.length === 1) {

                    var exactMatch = _.isEqual(
                        _.pick(frsInCol[0], ['x1', 'x2', 'y1', 'y2']),
                        _.pick(cols[j], ['x1', 'x2', 'y1', 'y2'])
                    );
                    if (exactMatch) {
                        rows[i].cols[j] = frsInCol[0];
                    } else {
                        rows[i].cols[j].rows = this.getTree(frsInCol, cols[j]).rows;
                    }


                } else {
                    rows[i].cols[j].rows = this.getTree(frsInCol, cols[j]).rows;
                }

            }

        }

        return {rows:rows};
    };



    /**
     * Returns HTML code for bootstrap rows and columns
     * @param {Object} node - the tree node to be translated as a grid
     * @returns {string} the html for the bootstrap rows
     */
    GridGenerator.prototype.readTree = function readTree(node) {

        var html = '';


        for (var i=0; i<node.rows.length; ++i) {

            var row = node.rows[i];

            html += '<div class="row">';

            var rowLength = row.x2 - row.x1 + 1;


            for (var j=0; j<row.cols.length; ++j) {

                var col = row.cols[j],
                    colLenght = col.x2 - col.x1 + 1,
                    l = colLenght / rowLength * 12,
                    h = col.y2 - col.y1 + 1,
                    isLeaf = !col.rows;

                var content, isMainFrame = false;
                
                if (col.pos !== undefined) {
                    isMainFrame = this.options.frames[col.pos].mainFrame;
                }
                
                if (isLeaf) {
                    content = '<span class="data-description">' + (_.escape(col.description) || '') + '</span>';
                } else {
                    content = this.readTree(col);
                }

                var classes = [
                    'col-xs-' + l,
                    (content && !col.rows? 'grid-slot' : 'empty-slot')
                ];
                if (isMainFrame) {
                    classes.push('topFrame');
                }

                html += '<div class="' + classes.join(' ') + '"';
                if (this.options.rowHeight) {
                    html += ' style="height:'+ (h * this.options.rowHeight)  +'px"';
                }
                if (isLeaf) {
                    html += ' data-pos="' + _.escape(col.pos+'') + '"';
                    html += ' data-description="' + _.escape(col.description) + '"';
                }

                html += '>' + content;
                html += '</div>';
            }
            html += '</div>';
        }

        return html;
    };


    /**
     * Returns HTML code for the bootstrap grid
     * @returns {string} the html for the bootstrap rows
     */
    GridGenerator.prototype.getHtml = function getHtml() {
        var root = this.getTree(this.totalFrames, this.totalBoundaries);

        return this.readTree(root, 0);
    };



    if (context) {
        context.GridGenerator = GridGenerator;
    }

    return GridGenerator;
})(window);
