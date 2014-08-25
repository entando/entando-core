(function () {
	var data = ENTANDO_MODEL_VOCABULARY;
	var include = function(arr1, item){
		if (!(arr1.indexOf(item) != -1)) arr1.push(item);
		return arr1;
	};
	var combine = function(arr1, arr2) {
		for (var i = 0, l = arr2.length; i < l; i++){
			include(arr1, arr2[i]);
		}
		return arr1;
	};
	var cloneArray =  function(arr){
		var i = arr.length;
		var clone = new Array(i);
		while (i--) {
			if (typeof arr[i] == 'array') { clone[i] = cloneArray(arr[i]); }
			else { clone[i] = arr[i] }
		}
		return clone;
	};
	var entandoHint = function(cm) {
		if (cm.somethingSelected()) return;
		var cursor = cm.getCursor(false);
		var textToComplete = cm.getLine(cursor.line);
		cursor.end = textToComplete.length;
		var token = cm.getTokenAt(cursor);
 		token.state = CodeMirror.innerMode(cm.getMode(), token.state).state;
		var namespace = namespaceParser(textToComplete.substring(0, cursor.ch), cursor.ch);
		var putPosition = {
			start: token.start,
			end: token.end
		}
		//
		var nsLastItem = namespace[namespace.length-1];
		if(namespace.length==1 && nsLastItem=='/') {
			putPosition.start=cursor.ch;
			putPosition.end=cursor.end;
		}
		if (namespace.length!=1) {
			var add = 0;
			if (nsLastItem=='/') { add = 1; }
			putPosition.start = (cursor.ch-(namespace[namespace.length-1].length)+add);
			putPosition.end = cursor.end;
		}
		//console.log('ns',namespace, putPosition.start, putPosition.end, '|', token.start, token.end);
		return {
			list: getVocabulary(namespace,cm.getValue()),
			from: CodeMirror.Pos(cursor.line, putPosition.start),
			to: CodeMirror.Pos(cursor.line, putPosition.end)
		};
	}

	var namespaceParser = function (nameSpaceString, caretPosition) {
		if (nameSpaceString === undefined) {
			nameSpaceString = "";
		}
		if (typeof caretPosition != "number") {
			caretPosition = nameSpaceString.length;
		}
		var namespace = [];
		var allowed = ["()", "$", "#"]; /* parser start */
		var positionStart = 0;
		var i = 0;
		for (i = caretPosition - 1; i > 0; --i) {
			var character = nameSpaceString.charAt(i);
			var previousCharacter = nameSpaceString.charAt(i + 1);
			if (character === undefined) {
				break;
			}
			if (character == "." && previousCharacter == ".") {
				positionStart = i + 1 + 1;
				break;
			}
			var cursorJump = 0;
			var endsWithAllowed = allowed.some(function (item) {
				if (item.length == 1) {
					return character == item;
				}
				else if (nameSpaceString.substring(i - item.length + 1, i + 1) == item) {
					//cursorJump = item.length+1;
					cursorJump = item.length - 1;
					return true;
				}
				else if (nameSpaceString.substring(i, i + item.length) == item) {
					return true;
				}
			});
			if (cursorJump > 0) {
				i = i - cursorJump;
				character = nameSpaceString[i];
				previousCharacter = nameSpaceString.charAt(i + 1);
				continue;
			}
			if (character != "." && !(/^\w$/.test(character) || endsWithAllowed)) {
				positionStart = i + 1;
				if (previousCharacter !== undefined) {
					var jumpPrevious = 0;
					if (previousCharacter == ".") {
						//if theres a dot ".", just move forward of 1 position and exit the loop.
						jumpPrevious = 1;
						positionStart = i + 1 + jumpPrevious;
						break;
					}
					var previousCharacterEndsWithAllowed = allowed.some(function (item) {
						if (item.length == 1) {
							if (previousCharacter == item) {
								jumpPrevious = 1;
								return true;
							}
						}
						//forward seek
						else if (nameSpaceString.substring(i, i + item.length) == item) {
							jumpPrevious = item.length;
							return true;
						}
						//back seek
						else if (nameSpaceString.substring(i - item.length + 1, i + 1) == item) {
							jumpPrevious = (-(item.length));
							return true;
						}
					});
					if (!previousCharacterEndsWithAllowed && !/^\w$/.test(previousCharacter)) {
						//here only allowed
						positionStart = i + 1 + jumpPrevious;
					}
				}
				break;
			}
		}
		if (positionStart > caretPosition) {
			positionStart = caretPosition;
		}
		nameSpaceString = nameSpaceString.substring(positionStart, caretPosition).trim();
		if (nameSpaceString.length > 0) {
			namespace = nameSpaceString.split(".");
			if (namespace[namespace.length - 1] === "") {
				namespace[namespace.length - 1] = "/";
			}
		}
		else {
			namespace = ["/"];
		} /* parser end */
		return namespace;
	};

	var getVocabulary = function(namespace,editor_full_text) {
		var currentNamespace = namespace;
		var extractedVocabulary = null;
		var vocabulary = data;
		var vocabularyManager_Extract = function (namespace, vocabulary) {
			if (namespace[0] === "") {
				namespace = cloneArray(namespace);
				namespace.shift();
			}
			var vocabularyFound = [];
			var found = null;
			var searchKey = null;
			if (namespace.length === 1) {
				found = vocabulary;
				if (namespace[0] != "/") {
					searchKey = namespace[0];
					searchKey = searchKey.replace(/\*/g, "\\\*");
					searchKey = searchKey.replace(/\./g, "\\\.");
					searchKey = searchKey.replace(/\?/g, "\\\?");
					searchKey = searchKey.replace(/\[/g, "\\\[");
					searchKey = searchKey.replace(/\]/g, "\\\]");
					searchKey = searchKey.replace(/\(/g, "\\\(");
					searchKey = searchKey.replace(/\)/g, "\\\)");
					searchKey = searchKey.replace(/\{/g, "\\\{");
					searchKey = searchKey.replace(/\}/g, "\\\}");
					searchKey = searchKey.replace(/\^/g, "\\\^");
					searchKey = searchKey.replace(/\$/g, "\\\$");
				}
			}
			else if (namespace.length > 1) {
				if (namespace[namespace.length - 1] != "/") {
					searchKey = namespace[namespace.length - 1];
					searchKey = searchKey.replace(/\|/g, "\\\|");
					searchKey = searchKey.replace(/\*/g, "\\\*");
					searchKey = searchKey.replace(/\./g, "\\\.");
					searchKey = searchKey.replace(/\?/g, "\\\?");
					searchKey = searchKey.replace(/\[/g, "\\\[");
					searchKey = searchKey.replace(/\]/g, "\\\]");
					searchKey = searchKey.replace(/\(/g, "\\\(");
					searchKey = searchKey.replace(/\)/g, "\\\)");
					searchKey = searchKey.replace(/\{/g, "\\\{");
					searchKey = searchKey.replace(/\}/g, "\\\}");
					searchKey = searchKey.replace(/\^/g, "\\\^");
					searchKey = searchKey.replace(/\$/g, "\\\$");
				}
				namespace = cloneArray(namespace);
				namespace.pop();
				var tempFound = vocabulary;
				for (var i = 0; i < namespace.length; i++) {
					try {
						tempFound = tempFound[namespace[i]];
					}
					catch (e) {
						tempFound = null;
					}
				}
				found = tempFound;
			}
			if (null !== found) {
				if (typeof found == "object") {
					var patt=new RegExp('^'+searchKey,'i');
					$.each(found, function(key, value) {
						if (searchKey === null || patt.test(key)) {
							vocabularyFound.push(key);
						}
					});
				}
				else if (typeof found == "array") {
					var patt=new RegExp('^'+searchKey,'i');
					$.each(found, function(index, item) {
						if (typeof item == "string" || typeof item == "number") {
							item = item.toString();
							if (item.length > 0) {
								if (searchKey === null || patt.test(item)) {
									vocabularyFound.push(item.toString());
								}
							}
						}
					});
				}
				vocabularyFound.sort();
			}
			return vocabularyFound;
		};
		extractedVocabulary = vocabularyManager_Extract(namespace, vocabulary);
		var completions = extractedVocabulary;
		if (namespace.length == 1) {
			if (namespace[namespace.length - 1] == "/" || completions.length == 0) {
				var discoveredKeywords = discoverWords(editor_full_text);
				for (var i = 0; i < discoveredKeywords.length; i++) {
					var currentDiscovered = discoveredKeywords[i];
					var isIn = false || (namespace[namespace.length - 1] == currentDiscovered);
					var x = 0;
					while (!isIn && x < completions.length) {
						if (currentDiscovered == completions[x]) {
							isIn = true;
						}
						x = x + 1;
					}
					if (!isIn) {
						if (namespace[namespace.length - 1] == "/") {
							completions.push(currentDiscovered);
						}
						else {
							var chk = namespace[namespace.length - 1];
							if (currentDiscovered.substring(0, chk.length) == chk) {
								completions.push(currentDiscovered);
							}
						}
					}
				}
			}
		}
		return extractedVocabulary;
	};

	var discoverWords = function(str) {
		var array = [];
		var utilSplit = function (arrayString, splitChar, reAppend) {
				var returnValue = [];
				for (var i = 0; i < arrayString.length; i++) {
					var current = arrayString[i].split(splitChar);
					for (var x = 0; x < current.length; x++) {
						if (current[x].length > 3) {
							returnValue.push(reAppend === true && x > 0 ? splitChar + current[x] : current[x]);
						}
					}
				}
				return returnValue;
			};
		str = str.replace(/[^\w\$\#\.]/g, " ");
		str = utilSplit([str], " ", false);
		str = utilSplit(str, "$", true);
		str = utilSplit(str, "#", true);
		array = str;
		array.sort();

		array = combine([], array)
		return array;
	};

	CodeMirror.entandoHint = entandoHint; // deprecated
	//CodeMirror.registerHelper("hint", "velocity", entandoHint);
	CodeMirror.registerHelper("hint", "velocity", entandoHint);

})();
