/**
 * @fileoverview logger wrapper
 */

var Logger = function(name) {
  this.logger = Packages.java.util.logging.Logger.getLogger(name);
};

Logger.prototype.getTrace = function() {
 var trace =  Packages.java.lang.Thread.currentThread().getStackTrace();
 for (var i = 0; i < trace.length; i++) {
   if (String(trace[i].getFileName()) == __filename) {
    return trace[i];
  }
 }
};

Logger.prototype.logit = function(level, message) {
  var trace = this.getTrace()
      , method = String(trace.getMethodName())
  ;

  // clean up method name
  method = method.replace(/^_c_/, "");
  method = method.replace(/_[0-9]+$/, "");
  this.logger.logp(level, trace.getFileName() + ":" + method, "Line " + trace.getLineNumber(), message);
};

Logger.prototype.finest = function(message) {
  this.logit(java.util.logging.Level.FINEST, message);
};

Logger.prototype.finer = function(message) {
  this.logit(java.util.logging.Level.FINER, message);
};

Logger.prototype.fine = function(message) {
  this.logit(java.util.logging.Level.FINE, message);
};

Logger.prototype.config = function(message) {
  this.logit(java.util.logging.Level.CONFIG, message);
};

Logger.prototype.info = function(message) {
  this.logit(java.util.logging.Level.INFO, message);
};

Logger.prototype.warning = function(message) {
  this.logit(java.util.logging.Level.WARNING, message);
};

Logger.prototype.severe = function(message) {
  this.logit(java.util.logging.Level.SEVERE, message);
};

exports.Logger = Logger;
