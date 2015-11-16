package com.renren.ugc.comment.strategy;

public enum QueryOrder {
    ASC {

        public boolean isDesc() {
            return false;
        }

        public String toString() {
            return "ASC";
        }

    },

    DESC {

        public boolean isDesc() {
            return true;
        }

        public String toString() {
            return "DESC";
        }

    };

    public abstract boolean isDesc();
}
